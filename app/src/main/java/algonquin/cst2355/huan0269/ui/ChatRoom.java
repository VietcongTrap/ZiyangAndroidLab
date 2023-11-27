package algonquin.cst2355.huan0269.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2355.huan0269.R;
import algonquin.cst2355.huan0269.data.ChatMessage;
import algonquin.cst2355.huan0269.data.ChatMessageDAO;
import algonquin.cst2355.huan0269.data.ChatRoomViewModel;
import algonquin.cst2355.huan0269.data.MessageDatabase;
import algonquin.cst2355.huan0269.databinding.ActivityChatRoomBinding;
import algonquin.cst2355.huan0269.databinding.ReceiveMessageBinding;
import algonquin.cst2355.huan0269.databinding.SentMessageBinding;


public class ChatRoom extends AppCompatActivity {

    ArrayList<ChatMessage> messages = null;

    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter = null;
    // used to select a row for display and deletion
    int selectedRow;
    ChatMessageDAO mDAO ;
    ChatRoomViewModel chatModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        // get the data from view model
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        chatModel.selectedMessage.observe(this, (newValue) -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newValue);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation,chatFragment)
                    .addToBackStack("Tag")
                    .commit();

        });
        // get everything from db
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<ChatMessage> fromDatabase = mDAO.getAllMessages();
            messages.addAll(fromDatabase);
        });
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<>());
        }
        setSupportActionBar(binding.myToolbar);

        // when click the Send button
        binding.button.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,true);
            // add to database
            Executor thread3 = Executors.newSingleThreadExecutor();
            thread3.execute(() -> {
                mDAO.insertMessage(chatMessage);
                messages.clear();
                List<ChatMessage> fromDatabase = mDAO.getAllMessages();
                messages.addAll(fromDatabase);
            });
            myAdapter.notifyItemChanged(messages.size()-1);
            //clear the input
            binding.textInput.setText("");
        });
        // when click the Receive button
        binding.receiveButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,false);
            Executor thread4 = Executors.newSingleThreadExecutor();
            thread4.execute(() -> {
                mDAO.insertMessage(chatMessage);
                messages.clear();
                List<ChatMessage> fromDatabase = mDAO.getAllMessages();
                messages.addAll(fromDatabase);
            });
            myAdapter.notifyItemChanged(messages.size()-1);
            //clear the input
            binding.textInput.setText("");
        });

        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
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
        };
        binding.myRecyclerView.setAdapter( myAdapter );

        // Allow scrolling
        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //which menu file
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() )
        {
            case R.id.item_1:
                //put your ChatMessage deletion code here. If you select this item, you should show the alert dialog
                //asking if the user wants to delete this message.

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message" + messageText.getText());
                builder.setTitle("Warning:");
                builder.setNegativeButton("No",(dialog,cl)->{ });
                builder.setPositiveButton("Yes",(dialog,cl)->{
                    Executor thread5 = Executors.newSingleThreadExecutor();
                    thread5.execute(new Runnable() {
                        @Override
                        public void run() {
                            mDAO.deleteMessage(m);
                        }
                    });
                    messages.remove(selectedRow);
                    myAdapter.notifyItemRemoved(selectedRow);
                    Snackbar.make(messageText,"You deleted message" + selectedRow, Snackbar.LENGTH_SHORT).setAction("undo", click -> {
                        messages.add(selectedRow,m);
                        myAdapter.notifyItemInserted(selectedRow);
                        Executor thread6 = Executors.newSingleThreadExecutor();
                        thread6.execute(new Runnable() {
                            @Override
                            public void run() {
                                mDAO.insertMessage(m);
                            }
                        });
                    }).show();
                });
                builder.create().show();

            case R.id.item_2:
                break;
        }

        return true;
    }

    // This represents a single row in the recycler
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> {
                int position =  getAdapterPosition();
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);
                selectedRow = position;
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message" + messageText.getText());
                builder.setTitle("Warning:");
                builder.setNegativeButton("No",(dialog,cl)->{ });
                builder.setPositiveButton("Yes",(dialog,cl)->{
                    Executor thread5 = Executors.newSingleThreadExecutor();
                    thread5.execute(new Runnable() {
                        @Override
                        public void run() {
                            mDAO.deleteMessage(m);
                        }
                    });
                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    Snackbar.make(messageText,"You deleted message" + position, Snackbar.LENGTH_SHORT).setAction("undo", click -> {
                        messages.add(position,m);
                        myAdapter.notifyItemInserted(position);
                        Executor thread6 = Executors.newSingleThreadExecutor();
                        thread6.execute(new Runnable() {
                            @Override
                            public void run() {
                                mDAO.insertMessage(m);
                            }
                        });
                    }).show();
                });
                builder.create().show();
                */
            });
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}