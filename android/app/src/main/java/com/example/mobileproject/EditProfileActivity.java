package com.example.mobileproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileproject.dto.UserData;
import com.example.mobileproject.spinners.CustomSpinnerAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.StringWriter;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;


public class EditProfileActivity extends AppCompatActivity {

    EditText editTextAge;
    EditText editTextCity;
    EditText editTextUsername;
    Spinner spinnerStatus;
    Spinner spinnerSex;
    Spinner hobbySpinner1;
    Spinner hobbySpinner2;
    Spinner hobbySpinner3;
    String selectedStatus;
    String selectedGender;
    String selectedHobby1;
    String selectedHobby2;
    String selectedHobby3;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner statusSpinner = findViewById(R.id.status_spinner);

        // Массив для адаптера
        String[] itemsStatus = new String[]{"Свободен", "В активном поиске", "В отношениях", "Женат", "Замужем"};

        // Кастомный адаптер
        CustomSpinnerAdapter adapterStatus = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsStatus);

        // Установка адаптера
        statusSpinner.setAdapter(adapterStatus);

        // Обработка выбора элемента
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                adapterStatus.setSelectedPosition(position);
                String selectedItemStatus = (String) parentView.getSelectedItem(); // Выбранный элемент к строке
                selectedStatus = selectedItemStatus; // Передача полученного значения в строковую переменную для JSON
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        Spinner genderSpinner = findViewById(R.id.genderSpinner);

        String[] itemsGender = new String[]{"Мужской", "Женский"};

        CustomSpinnerAdapter adapterGender = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsGender);

        genderSpinner.setAdapter(adapterGender);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                adapterGender.setSelectedPosition(position);
                String selectedItemGender = (String) parentView.getSelectedItem(); // Выбранный элемент к строке
                selectedGender = selectedItemGender; // Передача полученного значения в строковую переменную для JSON
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        Spinner hobby1Spinner = findViewById(R.id.hobby1_spinner);
        Spinner hobby2Spinner = findViewById(R.id.hobby2_spinner);
        Spinner hobby3Spinner = findViewById(R.id.hobby3_spinner);

        String[] itemsHobbys = new String[]{"Спорт", "Видеоигры", "Рыбалка", "Кулинария", "Чтение", "Рисование", "Музыка", ""};

        CustomSpinnerAdapter adapterHobby1 = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsHobbys);
        CustomSpinnerAdapter adapterHobby2 = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsHobbys);
        CustomSpinnerAdapter adapterHobby3 = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsHobbys);

        hobby1Spinner.setAdapter(adapterHobby1);
        hobby2Spinner.setAdapter(adapterHobby2);
        hobby3Spinner.setAdapter(adapterHobby3);
        hobby1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                adapterHobby1.setSelectedPosition(position);
                String selectedItemHobby1 = (String) parentView.getSelectedItem();
                selectedHobby1 = selectedItemHobby1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        hobby2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                adapterHobby2.setSelectedPosition(position);
                String selectedItemHobby2 = (String) parentView.getSelectedItem();
                selectedHobby2 = selectedItemHobby2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        hobby3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                adapterHobby3.setSelectedPosition(position);
                String selectedItemHobby3 = (String) parentView.getSelectedItem();
                selectedHobby3 = selectedItemHobby3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        avatar = findViewById(R.id.imageView3);
        Button selectPhotoButton = findViewById(R.id.select_photo_button);
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop());
            Glide.with(this)
                    .load(mImageUri)
                    .apply(requestOptions)
                    .into(avatar);
        }
    }

    public void onClickSaveProfileButton(View view) throws IOException {

        editTextAge = findViewById(R.id.editTextNumber3);
        editTextCity = findViewById(R.id.editTextTextPostalAddress);
        spinnerSex = findViewById(R.id.genderSpinner);
        spinnerStatus = findViewById(R.id.status_spinner);
        editTextUsername = findViewById(R.id.editUsername);
        hobbySpinner1 = findViewById(R.id.hobby1_spinner);
        hobbySpinner2 = findViewById(R.id.hobby2_spinner);
        hobbySpinner3 = findViewById(R.id.hobby3_spinner);

        UserData userData = new UserData();
        userData.setUsername(editTextUsername.getText().toString());
        userData.setAge(editTextAge.getText().toString());
        userData.setSex(spinnerSex.getSelectedItem().toString());
        userData.setCity(editTextCity.getText().toString());
        userData.setStatus(spinnerStatus.getSelectedItem().toString());
        userData.setHobby1(hobbySpinner1.getSelectedItem().toString());
        userData.setHobby2(hobbySpinner2.getSelectedItem().toString());
        userData.setHobby3(hobbySpinner3.getSelectedItem().toString());

        //TODO ПРЕОБРАЗОВАНИЕ ОБЪЕКТА КЛАССА В JSON
        //писать результат сериализации будем во Writer(StringWriter)
        StringWriter writer = new StringWriter();

        //это объект Jackson, который выполняет сериализацию
        ObjectMapper mapper = new ObjectMapper();

        // сама сериализация: 1-куда, 2-что
        mapper.writeValue(writer, userData);

        //преобразовываем все записанное во StringWriter в строку
        String result = writer.toString();
        //ВЫВОД JSON
        Toast toast = Toast.makeText(getApplicationContext(),
                result,
                Toast.LENGTH_SHORT);
        toast.show();


        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}