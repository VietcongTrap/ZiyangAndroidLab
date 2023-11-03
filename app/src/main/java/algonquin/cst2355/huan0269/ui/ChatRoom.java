package algonquin.cst2355.huan0269.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2355.huan0269.R;
import algonquin.cst2355.huan0269.data.ChatMessage;
import algonquin.cst2355.huan0269.data.ChatRoomViewModel;
import algonquin.cst2355.huan0269.databinding.ActivityChatRoomBinding;
import algonquin.cst2355.huan0269.databinding.ReceiveMessageBinding;
import algonquin.cst2355.huan0269.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ArrayList<ChatMessage> messages = null;

    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get the data from view model
        ChatRoomViewModel chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
        }
        // when click the Send button
        binding.button.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,true);
            messages.add((chatMessage));
            myAdapter.notifyItemChanged(messages.size()-1);
            //clear the input
            binding.textInput.setText("");

        });
        // when click the Receive button
        binding.receiveButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,false);
            messages.add((chatMessage));
            myAdapter.notifyItemChanged(messages.size()-1);
            //clear the input
            binding.textInput.setText("");
        });


        //Initialize the adapter
        binding.myRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
                    @NonNull
                    @Override
                    // populate the row
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        if (viewType == 0) {
                            SentMessageBinding newBinding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                            return new MyRowHolder(newBinding.getRoot());
                        } else {
                            ReceiveMessageBinding newBinding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                            return new MyRowHolder(newBinding.getRoot());
                        }
                    }

                    @Override
                    // replace the default text
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                        holder.messageText.setText(""+position);
                        holder.timeText.setText(""+position);

                        holder.messageText.setText(messages.get(position).getMessage());
                        holder.timeText.setText(messages.get(position).getTimeSent());

                    }

                    public int getItemViewType(int position){
                        // 1 if receive, 0 if send

                        if (messages.get(position).getIsSent()){
                            return 0;
                        } else {
                            return 1;
                        }


                    }
                    @Override
                    public int getItemCount() {
                        return messages.size();
                    }
                }

        );
        // Allow scrolling
        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    // This represents a single row in the recycler
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }


    }


}