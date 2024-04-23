package com.example.mobileproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileproject.profiles.Profile;
import com.example.mobileproject.requests.CreateProfile;
import com.example.mobileproject.spinners.CustomSpinnerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditProfileActivity extends AppCompatActivity {

    EditText editTextAge;
    EditText editTextCity;
    EditText editTextName;
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
    User userContext = UserContext.getInstance().getUser();
    ImageView avatar;
    boolean profileIsReceived = false;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

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


        Intent intent = getIntent();
        editTextName = findViewById(R.id.editUsername);
        editTextName.setText(intent.getStringExtra("name"));
        editTextAge = findViewById(R.id.editTextNumber3);
        editTextAge.setText(intent.getStringExtra("age"));
        editTextCity = findViewById(R.id.editTextTextPostalAddress);
        editTextCity.setText(intent.getStringExtra("city"));

        // SPINNER STATUS

        spinnerStatus = findViewById(R.id.status_spinner);
        // Массив для адаптера
        String[] itemsStatus = new String[]{"Свободен", "В поиске", "В отношениях", "Женат", "Замужем"};
        // Кастомный адаптер
        CustomSpinnerAdapter adapterStatus = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsStatus);
        // Установка адаптера
        spinnerStatus.setAdapter(adapterStatus);



        // Обработка выбора элемента
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        // Получаем текстовое значение из Intent
        String selectedValue = intent.getStringExtra("status");
        int spinnerPosition = adapterStatus.getPosition(selectedValue);
        spinnerStatus.setSelection(spinnerPosition);

        // SEX SPINNER
        spinnerSex = findViewById(R.id.genderSpinner);

        String[] itemsGender = new String[]{"Мужской", "Женский"};

        CustomSpinnerAdapter adapterGender = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsGender);

        spinnerSex.setAdapter(adapterGender);
        // Получаем текстовое значение из Intent
        String selectedValueSex = intent.getStringExtra("gender");
        int spinnerPositionSex = adapterGender.getPosition(selectedValueSex);
        spinnerSex.setSelection(spinnerPositionSex);

        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        hobbySpinner1 = findViewById(R.id.hobby1_spinner);
        hobbySpinner2 = findViewById(R.id.hobby2_spinner);
        hobbySpinner3 = findViewById(R.id.hobby3_spinner);

        String[] itemsHobbys = new String[]{"", "Спорт", "Видеоигры", "Рыбалка", "Кулинария", "Чтение", "Рисование", "Музыка"};

        CustomSpinnerAdapter adapterHobby1 = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsHobbys);
        CustomSpinnerAdapter adapterHobby2 = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsHobbys);
        CustomSpinnerAdapter adapterHobby3 = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsHobbys);

        hobbySpinner1.setAdapter(adapterHobby1);
        String selectedValueTag1 = intent.getStringExtra("tag1");
        int spinnerPositionTag1 = adapterHobby1.getPosition(selectedValueTag1);
        hobbySpinner1.setSelection(spinnerPositionTag1);

        hobbySpinner2.setAdapter(adapterHobby2);
        String selectedValueTag2 = intent.getStringExtra("tag2");
        int spinnerPositionTag2 = adapterHobby2.getPosition(selectedValueTag2);
        hobbySpinner2.setSelection(spinnerPositionTag2);

        hobbySpinner3.setAdapter(adapterHobby3);
        String selectedValueTag3 = intent.getStringExtra("tag3");
        int spinnerPositionTag3 = adapterHobby3.getPosition(selectedValueTag3);
        hobbySpinner3.setSelection(spinnerPositionTag3);

        hobbySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        hobbySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        hobbySpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.111.92:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Получение аватарки от сервера
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Profile> call = apiService.getProfile(userContext);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    profileIsReceived = true;
                    Profile profile = response.body();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "УСПЕШНО",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    // Обработка изображения
                    String codedAvatar = profile.getPhoto();
                    byte[] decodedString = Base64.decode(codedAvatar, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    //avatar = findViewById(R.id.imageView3);
                    avatar.setImageBitmap(decodedByte);

                } else {
                    profileIsReceived = false;
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Данные не были получены",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                profileIsReceived = false;
                Toast toast = Toast.makeText(getApplicationContext(),
                        "ОШИБКА",
                        Toast.LENGTH_SHORT);
                toast.show();
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
        editTextName = findViewById(R.id.editUsername);
        hobbySpinner1 = findViewById(R.id.hobby1_spinner);
        hobbySpinner2 = findViewById(R.id.hobby2_spinner);
        hobbySpinner3 = findViewById(R.id.hobby3_spinner);

        // Получение аватарки в виде BitArray
        avatar = (ImageView) findViewById(R.id.imageView3);
        Bitmap bitmapAvatar = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapAvatar.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedAvatar = Base64.encodeToString(byteArray, Base64.DEFAULT);


        //int valid_age = Integer.parseInt(editTextAge.getText().toString());

        //if (valid_age >= 16) {
        Profile profile = new Profile();
//        profile.setUsername(userContext.getUsername());
//        profile.setPassword(userContext.getPassword());
        profile.setName(editTextName.getText().toString());
        profile.setAge(Integer.parseInt(editTextAge.getText().toString()));
        profile.setSex(spinnerSex.getSelectedItem().toString());
        profile.setCity(editTextCity.getText().toString());
        profile.setStatus(spinnerStatus.getSelectedItem().toString());
        List<String> hobbies = new ArrayList<>();
        hobbies.add(hobbySpinner1.getSelectedItem().toString());
        hobbies.add(hobbySpinner2.getSelectedItem().toString());
        hobbies.add(hobbySpinner3.getSelectedItem().toString());
        profile.setHobbies(hobbies);

        profile.setPhoto(encodedAvatar);

        CreateProfile createProfile = new CreateProfile(profile, userContext);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.111.92:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        if (profileIsReceived) {
            apiService.patchProfile(createProfile).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(EditProfileActivity.this, MainMenu.class);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Успешно",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                response.toString(),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ошибка соединения",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } else {
            apiService.postProfile(createProfile).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(EditProfileActivity.this, MainMenu.class);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Успешно",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                response.toString(),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ошибка соединения",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }


//            Intent intent = new Intent(this, ProfileActivity.class);
//            startActivity(intent);
//            finish();
        //}
//        else{
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "Возраст не может быть ниже 16",
//                    Toast.LENGTH_SHORT);
//            toast.show();
        //}
    }
}