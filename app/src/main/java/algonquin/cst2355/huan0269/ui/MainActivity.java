package algonquin.cst2355.huan0269.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=5abd7f98683fe7ce7a0e2b51f20387ec&units=metric";

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        (response) -> {
                            try {
                                JSONObject coord = response.getJSONObject("coord");
                                JSONArray weatherArray = response.getJSONArray("weather");

                                String description = "";
                                String iconName = "";
                                for (int i=0; i<weatherArray.length(); i++){
                                    JSONObject thisPosition = weatherArray.getJSONObject(i);
                                    iconName = thisPosition.getString("icon");
                                    description = thisPosition.getString("description");
                                }
                                String imageUrl = "https://openweathermap.org/img/w/" + iconName + ".png";
                                String finalIconName = iconName;
                                Bitmap image = null;
                                String pathname = getFilesDir()+"/"+iconName+".png";
                                File file = new File(pathname);
                                if(file.exists())
                                {
                                    image = BitmapFactory.decodeFile(pathname);
                                    binding.weatherImage.setImageBitmap(image);
                                } else {
                                    String finalIconName1 = iconName;
                                    ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            try {
                                            // Do something with loaded bitmap...
                                            Log.d("image received","got the image");
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(finalIconName1 +".png", Activity.MODE_PRIVATE));

                                                binding.weatherImage.setImageBitmap(bitmap);
                                            } catch (FileNotFoundException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    } , 1024, 1024, ImageView.ScaleType.CENTER, null,
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // Do something with error response
                                                    error.printStackTrace();
                                                    Log.d("error", error.getMessage());
                                                }
                                            }
                                 );
                                    queue.add(imgReq);
                                }
                                JSONObject mainObject = response.getJSONObject( "main" );
                                int vis = response.getInt("visibility");
                                String name = response.getString("name");

                                double current = mainObject.getDouble("temp");
                                double min = mainObject.getDouble("temp_min");
                                double max = mainObject.getDouble("temp_max");
                                int humidity = mainObject.getInt("humidity");
                                String finalDescription = description;
                                runOnUiThread( (  )  -> {
                                    binding.weatherImage.setVisibility(View.VISIBLE);
                                    binding.temp.setText("The current temperature is " + current);
                                    binding.temp.setVisibility(View.VISIBLE);
                                    binding.minTemp.setText("The current minimal temperature " + min);
                                    binding.minTemp.setVisibility(View.VISIBLE);
                                    binding.maxTemp.setVisibility(View.VISIBLE);
                                    binding.maxTemp.setText("The current maximum temperature " + max);
                                    binding.humidity.setText("The current humidity is " + humidity);
                                    binding.humidity.setVisibility(View.VISIBLE);
                                    binding.desc.setText(finalDescription);
                                    binding.desc.setVisibility(View.VISIBLE);
                                });

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