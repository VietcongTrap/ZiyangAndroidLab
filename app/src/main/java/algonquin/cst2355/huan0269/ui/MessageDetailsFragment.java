package algonquin.cst2355.huan0269.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2355.huan0269.data.ChatMessage;
import algonquin.cst2355.huan0269.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    ChatMessage selected;
    public MessageDetailsFragment(ChatMessage m){
        selected = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.messageDetail.setText(selected.getMessage());

        binding.databaseId.setText("id= " + selected.getId());
        binding.timeDetail.setText(selected.getTimeSent());
        if (selected.getIsSent()){
            binding.sendReceive.setText("Sent");
        } else {
            binding.sendReceive.setText("Received");
        }
        return binding.getRoot();
    }
}
