package algonquin.cst2355.huan0269.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
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
        ImageButton ib = binding.myImageButton;

        ib.setOnClickListener(clck->{
            mytext.setText("You clicked the image!");
            int width = ib.getWidth();
            int height = ib.getHeight();
            CharSequence imgToast = "The width = " + width + " and height = " + height ;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this /* MyActivity */, imgToast, duration);
            toast.show();
        });

        // monitor the changes on the three compound buttons
        rbt.setOnCheckedChangeListener((buttonView, onOrOff) -> {
            viewModel.onOrOff.postValue(onOrOff);
        });
        sw.setOnCheckedChangeListener((buttonView, onOrOff) -> {
            viewModel.onOrOff.postValue(onOrOff);
        });
        cb.setOnCheckedChangeListener((buttonView, onOrOff) -> {
            viewModel.onOrOff.postValue(onOrOff);
        });
        // set the changes
        viewModel.onOrOff.observe(this,onOrOff->{
            cb.setChecked(onOrOff);
            sw.setChecked(onOrOff);
            rbt.setChecked(onOrOff);
            CharSequence switchToast = "The value is!"+ onOrOff;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this /* MyActivity */, switchToast, duration);
            toast.show();
        });



        //When viewModel gets its value changed this will change mytext
        viewModel.userString.observe(this, s -> {
            mytext.setText("Your edit text has "+ s );
        } );
        //OnClickListener
        btn.setOnClickListener(v -> {
            String editString = et.getText().toString(); //read what the user typed
            viewModel.userString.postValue(editString); //set the value, and notify observers
            btn.setText("You clicked the button");
        });
    }
}