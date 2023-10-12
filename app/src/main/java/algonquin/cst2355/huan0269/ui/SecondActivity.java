package algonquin.cst2355.huan0269.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2355.huan0269.R;
import algonquin.cst2355.huan0269.data.MainViewModel;
import algonquin.cst2355.huan0269.databinding.ActivityMainBinding;
import algonquin.cst2355.huan0269.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        //this is the only function call, loads stuff onto screen
        TextView header = binding.textView;
        header.setText("Welcome back "+ emailAddress );
        setContentView(binding.getRoot());


        Button cambtn = binding.button2;
        EditText et =binding.editTextPhone;
        Button callbtn = binding.button;
        ImageView profileImage = binding.imageView;

        File file = new File( getFilesDir(), "Picture.png");
        if(file.exists())
        {
            Bitmap theImage = BitmapFactory.decodeFile("/data/data/algonquin.cst2355.huan0269/files/Picture.png");
            profileImage.setImageBitmap(theImage);
        }
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String telephoneNum = prefs.getString("Telenum","");
        SharedPreferences.Editor editor2 = prefs.edit();
        et.setText(telephoneNum);
        //OnClickListener
        callbtn.setOnClickListener(v -> {
            String phoneNumber   = et.getText().toString(); //read what the user typed
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            editor2.putString("Telenum", et.getText().toString());
            editor2.apply();

            startActivity(call);
        });
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);
                            FileOutputStream fOut = null;

                            try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (IOException e)
                            { e.printStackTrace();
                            }
                        }
                    }
                });
        cambtn.setOnClickListener(v -> {
            Log.i(TAG,"Cam button clicked");
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        });
    }
}
