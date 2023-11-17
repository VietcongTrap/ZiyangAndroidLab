package algonquin.cst2355.huan0269.data;

import androidx.annotation.Nullable;
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
    boolean isSent;

    public ChatMessage(String message, String timeSent, boolean isSent)
    {
        this.message = message;
        this.timeSent = timeSent;
        this.isSent = isSent;
    }

    public String getMessage() {
        return message;
    }
    public String getTimeSent() {
        return timeSent;
    }
    public boolean getIsSent(){
        return isSent;
    }
    public int getId(){return id;}
}
