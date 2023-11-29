package algonquin.cst2355.huan0269.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2355.huan0269.databinding.ActivityMainBinding;

/**
 * This is the main activity handling the main page of the app.
 * It allows user to enter password and check its validity.
 * @author Ziyang Huang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private EditText et = null;
    protected String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        algonquin.cst2355.huan0269.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        RequestQueue queue;
        //This part goes at the top of the onCreate function:
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());

        TextView tv = binding.myTextView;
        et = binding.myEditText;

        Button btn = binding.myButton;

        btn.setOnClickListener(clk -> {
            try {
                cityName = URLEncoder.encode(et.getText().toString(), "UTF-8");
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
                /*
                {
  "coord": {
    "lon": -74.006,
    "lat": 40.7143
  },
  "weather": [
    {
      "id": 500,
      "main": "Rain",
      "description": "light rain",
      "icon": "10n"
    },
    {
      "id": 701,
      "main": "Mist",
      "description": "mist",
      "icon": "50n"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 11.47,
    "feels_like": 10.97,
    "temp_min": 9.23,
    "temp_max": 12.94,
    "pressure": 1002,
    "humidity": 88
  },
  "visibility": 4828,
  "wind": {
    "speed": 14.31,
    "deg": 117,
    "gust": 18.78
  },
  "rain": {
    "1h": 0.16
  },
  "clouds": {
    "all": 100
  },
  "dt": 1701057672,
  "sys": {
    "type": 2,
    "id": 2008101,
    "country": "US",
    "sunrise": 1700999719,
    "sunset": 1701034295
  },
  "timezone": -18000,
  "id": 5128581,
  "name": "New York",
  "cod": 200
}
                 */

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        (response) -> {
                            try {
                                JSONObject coord = response.getJSONObject("coord");
                                JSONArray weatherArray = response.getJSONArray("weather");

                                String description;
                                String iconName = "";
                                for (int i=0; i<weatherArray.length(); i++){
                                    JSONObject thisPosition = weatherArray.getJSONObject(i);
                                    iconName = thisPosition.getString("icon");
                                    description = thisPosition.getString("description");
                                }
                                String imageUrl = "http://openweathermap.org/img/w/" + iconName + ".png";
                                ImageRequest imgReq = new ImageRequest(imageUrl, bitmap -> {
                                    // Do something with loaded bitmap...
                                    Log.d("image received", "got the image");
                                    binding.weatherImage.setImageBitmap(bitmap);
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        (error ) -> {
                                            Log.d("error", "image not downloaded");
                                        });
                                queue.add(imgReq);
                                JSONObject mainObject = response.getJSONObject( "main" );
                                int vis = response.getInt("visibility");
                                String name = response.getString("name");
                                double current = mainObject.getDouble("temp");
                                double min = mainObject.getDouble("temp_min");
                                double max = mainObject.getDouble("temp_max");
                                int humidity = mainObject.getInt("humidity");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        },
                        (error) -> {
                        });

                queue.add(request);

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException();
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