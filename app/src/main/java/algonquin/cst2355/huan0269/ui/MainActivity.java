package algonquin.cst2355.huan0269.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import algonquin.cst2355.huan0269.data.MainViewModel;
import algonquin.cst2355.huan0269.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG,"main is now created ");
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //this is the only function call, loads stuff onto screen
        setContentView(binding.getRoot());
        TextView mytext = binding.myTextView; //same as getElementById() in HTML
        Button btn = binding.myButton; //must not be null
        EditText et = binding.myEditText;

        String LoginName = "";
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString(LoginName,"");
        SharedPreferences.Editor editor = prefs.edit();
        et.setText(emailAddress);
        //OnClickListener
        btn.setOnClickListener(v -> {
            Log.i(TAG,"click the button");
            Intent nextPage = new Intent(MainActivity.this,SecondActivity.class);
            editor.putString(LoginName, et.getText().toString());
            editor.apply();

            nextPage.putExtra("EmailAddress",et.getText().toString());
            startActivity(nextPage);

        });
    }
    protected void onStart(){
        super.onStart();
        Log.i(TAG,"main is now responding");
    }
    protected void onResume(){
        super.onResume();
        Log.i(TAG,"main has resume activity");
    }

    @Override
    protected void onPause(){
        super.onPause();

        Log.i(TAG,"main is no longer ");
    }
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "main is no longer visible");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "main is destroyed");
    }
}