package algonquin.cst2355.huan0269.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import algonquin.cst2355.huan0269.R;
import algonquin.cst2355.huan0269.databinding.ActivityMainBinding;

/**
 * This is the main activity handling the main page of the app.
 * It allows user to enter password and check its validity.
 * @author Ziyang Huang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * This is the text on the center of the screen
     */
    private TextView tv = null;
    /**
     * This allows user to enter the password.
     */
    private EditText et = null;
    /**
     * This button when clicked will activate the validity checking function
     */
    private  Button btn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.myTextView);
        et = findViewById(R.id.myEditText);
       btn = findViewById(R.id.myButton);

        btn.setOnClickListener( clk -> {
            String password = et.getText().toString();
            checkPasswordValidity (password);
            if ( checkPasswordValidity (password) ){
                tv.setText("Your pass word meets the requirements");
            } else {
                tv.setText("You Shall NOT Pass");
            }
        });

    }

    /**
     * The method is used to check if a password has an uppercase, a lowercase
     * a digit and a special character.
     * @param pw the password String that is going to be checked
     * @return Return true if valid, false if invalid
     */
    boolean checkPasswordValidity (String pw){
        boolean foundUppercase = false;
        boolean foundLowercase = false;
        boolean foundNum = false;
        boolean foundSpecial = false;
        for (int i = 0; i<pw.length(); i++){
           char thisChar = pw.charAt(i);
           if ( Character.isUpperCase(thisChar)){
               foundUppercase = true;
           }
            if ( Character.isLowerCase(thisChar)){
                foundLowercase = true;
            }
            if ( Character.isDigit(thisChar)){
                foundNum = true;
            }
            if ( isSpecialCharacter(thisChar)){
                foundSpecial = true;
            }
        }
        if(!foundUppercase) {
            Toast.makeText(this,"upper case letter missing",Toast.LENGTH_SHORT).show(); ;// Say that they are missing an upper case letter;
            return false;
        } else if(!foundLowercase) {
            Toast.makeText(this,"lower case letter missing",Toast.LENGTH_SHORT).show(); ;// Say that they are missing an upper case letter;
            return false;
        }
        else if( !foundNum)  {
            Toast.makeText(this,"number missing",Toast.LENGTH_SHORT).show(); ;// Say that they are missing an upper case letter;
            return false;
        }
        else if(!foundSpecial)    {
            Toast.makeText(this,"special character missing",Toast.LENGTH_SHORT).show(); ;// Say that they are missing an upper case letter;
            return false;
        }
        else
            return true; //only get here if they're all true
    }

    /**
     * This method is made to check if there is a special character.
     * @param c the input character to be checked if it is a special character
     * @return true if special character, false if not
     */
    boolean isSpecialCharacter(char c){
        switch (c){
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}