package com.example.mobileproject.Chat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;

public class GeoLocationActivity extends AppCompatActivity {

    private MapView mapView;
    private TextView contactName;
    private CameraListener cameraListener;
    private MapObjectTapListener placemarkTapListener;
    private InputListener inputListener;
    private MapObjectCollection mapObjects;
    private List<PlacemarkMapObject> placemarkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_geo_location);

        mapView = findViewById(R.id.YandexMap);
        placemarkList = new ArrayList<>();

        // Установка начальной позиции камеры
        mapView.getMap().move(
                new CameraPosition(new Point(55.613432, 37.744395), 17.0f, 150.0f, 30.0f)
        );

        // Получение имени контакта из интента
        String contactName = getIntent().getStringExtra("contact_name");
        TextView textViewName = findViewById(R.id.dialogNameTextView);

        if (textViewName != null) {
            if (contactName != null) {
                textViewName.setText(contactName);
            } else {
                textViewName.setText("Аноним");
            }
        }

        // Создание ImageProvider из ресурса
        ImageProvider imageProvider = ImageProvider.fromResource(this, R.drawable.geopos);

        // Получение коллекции объектов карты
        mapObjects = mapView.getMap().getMapObjects();

        // Добавление метки на карту
        PlacemarkMapObject placemark = mapObjects.addPlacemark(new Point(59.935493, 30.327392));
        placemark.setIcon(imageProvider);
        placemarkList.add(placemark);

        // Создание и установка CameraListener
        cameraListener = new CameraListener() {
            @Override
            public void onCameraPositionChanged(com.yandex.mapkit.map.Map map, CameraPosition cameraPosition, CameraUpdateReason cameraUpdateReason, boolean finished) {
                // Ваш код здесь
            }
        };
        mapView.getMap().addCameraListener(cameraListener);

        // Создание и установка MapObjectTapListener
        placemarkTapListener = new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(com.yandex.mapkit.map.MapObject mapObject, Point point) {
                // Сохранение координат в буфер обмена
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Coordinates", point.getLongitude() + " " + point.getLatitude());
                clipboard.setPrimaryClip(clip);

                // Показать Toast сообщение
                Toast.makeText(GeoLocationActivity.this, "Координаты в буфере обмена", Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        placemark.addTapListener(placemarkTapListener);

        // Создание и установка InputListener для обработки нажатий на карту
        inputListener = new InputListener() {
            @Override
            public void onMapTap(com.yandex.mapkit.map.Map map, Point point) {
                // Добавление метки в точке нажатия
                PlacemarkMapObject newPlacemark = mapObjects.addPlacemark(point);
                newPlacemark.setIcon(imageProvider);
                newPlacemark.addTapListener(placemarkTapListener);
                placemarkList.add(newPlacemark);
            }

            @Override
            public void onMapLongTap(com.yandex.mapkit.map.Map map, Point point) {
                // Обработка длинного нажатия, если необходимо
            }
        };
        mapView.getMap().addInputListener(inputListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
