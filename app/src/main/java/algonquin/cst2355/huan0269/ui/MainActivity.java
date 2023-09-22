package algonquin.cst2355.huan0269.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ImageView;

import algonquin.cst2355.huan0269.data.MainViewModel;
import algonquin.cst2355.huan0269.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //this is the only function call, loads stuff onto screen
        setContentView(binding.getRoot());


        TextView mytext = binding.myTextView; //same as getElementById() in HTML
        Button btn = binding.myButton; //must not be null
        EditText et = binding.myEditText;
        ImageView mv = binding.myImageView;
        RadioButton rbt = binding.myRadioButton;
        Switch sw = binding.mySwitch;
        CheckBox cb = binding.myCheckBox;

        // monitor the changes on the three compound buttons
        rbt.setOnCheckedChangeListener((buttonView, b) -> {
            viewModel.onOrOff.postValue(b);
        });
        sw.setOnCheckedChangeListener((buttonView, b) -> {
            viewModel.onOrOff.postValue(b);
        });
        cb.setOnCheckedChangeListener((buttonView, b) -> {
            viewModel.onOrOff.postValue(b);
        });
        // set the changes
        viewModel.onOrOff.observe(this,newBool->{
            cb.setChecked(newBool);
            sw.setChecked(newBool);
            rbt.setChecked(newBool);
        });


        //When viewModel gets its value changed this will change mytext
        viewModel.userString.observe(this, s -> {
            mytext.setText("Your edit text has"+ s );
        } );
        //OnClickListener
        btn.setOnClickListener(v -> {
            String editString = et.getText().toString(); //read what the user typed
            viewModel.userString.postValue("Your edit text has"+editString); //set the value, and notify observers
            btn.setText("You clicked the button");
        });
    }
}