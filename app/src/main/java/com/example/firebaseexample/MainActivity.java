package com.example.firebaseexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText edittextTitle;
    private EditText edittextDescrption;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentReference noteRef = db.document("Notebook/My First NoteBook");

    //private ListenerRegistration noteListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittextTitle = findViewById(R.id.edit_text_title);
        edittextDescrption = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(MainActivity.this, "Error while running", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,e.toString());
                    return;
                }
                if(documentSnapshot.exists()){
                    Note note = documentSnapshot.toObject(Note.class);
                    String title = note.getTitle();
                    String description = note.getDescription();

                    textViewData.setText("Title : "+title+"\n"+"Description : "+description);
                }
            }
        });*/

        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    return;
                }
                String data= "";
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);

                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description = note.getDescription();
                    data+="ID:"+documentId+"\nTitle : "+title+"\ndescription : "+description+"\n\n";
                }

                textViewData.setText(data);
            }
        });
    }



    public void saveNote(View v){
        String title = edittextTitle.getText().toString();
        String description = edittextDescrption.getText().toString();

        //Map<String,Object> note = new HashMap<>();
        //note.put(KEY_TITLE,title);
        //note.put(KEY_DESCRIPTION,description);

        Note note = new Note(title,description);

        db.collection("Notebook").document("My First NoteBook").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });

    }
    public void addNote(View v){
        String title = edittextTitle.getText().toString();
        String description = edittextDescrption.getText().toString();

        //Map<String,Object> note = new HashMap<>();
        //note.put(KEY_TITLE,title);
        //note.put(KEY_DESCRIPTION,description);

        Note note = new Note(title,description);

        notebookRef.add(note);

    }

    public void UpDateDescription(View v){
        String description = edittextDescrption.getText().toString();

        //Map<String,Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION,description);

        //noteRef.set(note, SetOptions.merge());
        noteRef.update(KEY_DESCRIPTION,description);

    }
    public void loadNote(View v){
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            //String title = documentSnapshot.getString(KEY_TITLE);
                           // String description = documentSnapshot.getString(KEY_DESCRIPTION);
                            Note note = documentSnapshot.toObject(Note.class);
                            String title = note.getTitle();
                            String description = note.getDescription();

                            textViewData.setText("Title : "+title+"\n"+"Description : "+description);
                        }else{
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR!!!!!!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }
    public void loadNotes(View v){
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data= "";
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();
                            String title = note.getTitle();
                            String description = note.getDescription();
                            data+="ID:"+documentId+"\nTitle : "+title+"\ndescription : "+description+"\n\n";
                        }
                        textViewData.setText(data);
                    }
                });
    }
}