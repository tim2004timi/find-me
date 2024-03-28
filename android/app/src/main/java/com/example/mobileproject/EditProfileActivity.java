package com.example.mobileproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileproject.dto.UserData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class EditProfileActivity extends AppCompatActivity {

    EditText editTextAge;
    EditText editTextCity;
    EditText editTextUsername;
    Spinner spinnerStatus;
    Spinner spinnerSex;

    private ActivityResultLauncher<String> galleryLauncher;

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
        Spinner genderSpinner = findViewById(R.id.genderSpinner);

        String[] statusOptions = {"Свободен", "В активном поиске", "В отношениях", "Женат", "Замужем"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        String[] genderOptions = {"Мужской", "Женский"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        Button selectPhotoButton = findViewById(R.id.select_photo_button);
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        // поле обработки выбранного изображение из галереи
                        // result содержит URI выбранного изображения
                    }
                });
    }

    public void onClickSaveProfileButton(View view) throws IOException {

        editTextAge = findViewById(R.id.editTextNumber3);
        editTextCity = findViewById(R.id.editTextTextPostalAddress);
        spinnerSex = findViewById(R.id.genderSpinner);
        spinnerStatus = findViewById(R.id.status_spinner);
        editTextUsername = findViewById(R.id.editUsername);

        UserData userData = new UserData();
        userData.setUsername(editTextUsername.getText().toString());
        userData.setAge(editTextAge.getText().toString());
        userData.setSex(spinnerSex.getSelectedItem().toString());
        userData.setCity(editTextCity.getText().toString());
        userData.setStatus(spinnerStatus.getSelectedItem().toString());

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

    public void onClickeditPhoto(View view){
    }
}