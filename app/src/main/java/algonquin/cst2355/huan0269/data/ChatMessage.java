package algonquin.cst2355.huan0269.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="message")
    String message;
    @ColumnInfo(name="timeSent")
    String timeSent;
    @ColumnInfo(name="isSent")
    boolean isSentButton;
    public ChatMessage(String m, String t, boolean sent)
    {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public String getMessage() {
        return message;
    }
    public String getTimeSent() {
        return timeSent;
    }
    public boolean getIsSent(){
        return isSentButton;
    }
}
