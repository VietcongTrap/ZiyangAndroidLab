package algonquin.cst2355.huan0269;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2355.huan0269.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate( getLayoutInflater() );

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //this is the only function call, loads stuff onto screen
        setContentView( binding.getRoot() );



        TextView tv =binding.myTextView; //same as getElementById() in HTML
        Button b =  binding.myButton; //must not be null
        EditText et =binding.myEditText;

        //this will be called when the value changes:
        viewModel.userString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv.setText(s);
            }
        });
        et.setText( viewModel.userString.getValue() );
        //OnClickListener     //anonymnous class
        b.setOnClickListener(v -> {
            //only run when you click the button

            String string = et.getText().toString(); //read what the user typed
            viewModel.userString.postValue( string ); //set the value, and notify observers
            b.setText("You clicked the button");
        });
}