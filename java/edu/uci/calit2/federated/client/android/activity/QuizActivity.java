package edu.uci.calit2.federated.client.android.activity;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import edu.uci.calit2.federated.R;
import edu.uci.calit2.federated.client.android.fragment.PrivacyLeaksReportFragment;
import edu.uci.calit2.federated.client.android.fragment.RealTimeFragment;
/**
 * @author Alejandro Aguilera Alcalde 2021
 */
public class QuizActivity extends AppCompatActivity {

    private QuestionLibrary mQuestionLibrary = new QuestionLibrary();

    private TextView mScoreView;
    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private Button mButtonChoice3;
    private Button quit;
    private Button back;
    private String mAnswer;
    private int mScore = 0;
    private int mQuestionNumber = 0;

    boolean actualizacion_datos_activity = false;



    //pasar variables a otra actividad:
    public static final String PLOCATION = "Plocation";
    public static final String PEMAIL = "Pemail";
    public static final String PDEVICE = "Pdevice";
    public static final String PIMEI = "Pimei";
    public static final String PSERIALNUMBER = "Pserialnumber";
    public static final String PMACADDRESS = "Pmacaddresss";
    public static final String PADVERTISER = "Padvertiser";

    public static final String ACTUALIZACION_DATOS_ACTIVITY = "actualizacion_datos_activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mScoreView = (TextView)findViewById(R.id.score);
        mQuestionView = (TextView)findViewById(R.id.question);
        mButtonChoice1 = (Button)findViewById(R.id.choice1);
        mButtonChoice2 = (Button)findViewById(R.id.choice2);
        mButtonChoice3 = (Button)findViewById(R.id.choice3);
        quit = (Button)findViewById(R.id.quit);
        back = (Button)findViewById(R.id.back);

        updateQuestion();
        Intent intent = getIntent();

        //Start of Button Listener for Button1
        mButtonChoice1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //My logic for Button goes in here
                if(mQuestionView.getText() !="Finished, just answer a few questions more in order to know what you think and expect about the app. EVERYTHING IS ANONYMOUS.") {
                    if (mButtonChoice1.getText() == mAnswer) {
                        mScore = mScore + 1;
                        updateScore(mScore);
                        updateQuestion();
                        PrivacyLeaksReportFragment.array_quizz.add(1);
                        PrivacyLeaksReportFragment.Uncomfortable++;
                        RealTimeFragment.Uncomfortable++;

                        //This line of code is optiona
                        //    Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                        updateQuestion();
                    }
                }
            }
        });

        //End of Button Listener for Button1

        //Start of Button Listener for Button2
        mButtonChoice2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //My logic for Button goes in here
                if(mQuestionView.getText() !="Finished, just answer a few questions more in order to know what you think and expect about the app. EVERYTHING IS ANONYMOUS.") {
                    if (mButtonChoice2.getText() == mAnswer) {
                        mScore = mScore + 1;
                        updateScore(mScore);
                        updateQuestion();
                        PrivacyLeaksReportFragment.array_quizz.add(2);
                        PrivacyLeaksReportFragment.Middle++;
                        RealTimeFragment.Middle++;

                        //This line of code is optiona
                        //   Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                        updateQuestion();
                    }
                }
            }
        });

        //End of Button Listener for Button2


        //Start of Button Listener for Button3
        mButtonChoice3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //My logic for Button goes in here
                if(mQuestionView.getText() !="Finished, just answer a few questions more in order to know what you think and expect about the app. EVERYTHING IS ANONYMOUS.") {
                    if (mButtonChoice3.getText() == mAnswer) {
                        mScore = mScore + 1;
                        updateScore(mScore);
                        updateQuestion();
                        PrivacyLeaksReportFragment.array_quizz.add(3);
                        PrivacyLeaksReportFragment.Comfortable++;
                        RealTimeFragment.Comfortable++;
                        //This line of code is optiona
                        //  Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();

                    } else {
                        //   Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                        updateQuestion();
                    }
                }
            }
        });

        //End of Button Listener for Button3
        //BOTON VOLVER
        quit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                if(mQuestionView.getText() =="Finished, just answer a few questions more in order to know what you think and expect about the app. EVERYTHING IS ANONYMOUS.") {


                    /*
                   //para volver tras finished, 13/04/2021
                    Intent intent = new Intent(QuizActivity.this, AntMonitorMainActivity.class);


                    actualizacion_datos_activity = true;
                    PrivacyLeaksReportFragment.quiz_inicio_realizado = true;
                    intent.putExtra(ACTUALIZACION_DATOS_ACTIVITY, actualizacion_datos_activity);
                    startActivity(intent);

                     */

                    //test para de un cuestionario a otro.

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSeLenLYca4odpz6awZcu73v8g9FcGz1byoKZQeut35T6qVIhQ/viewform?usp=pp_url&entry.722205137="+ RealTimeFragment.ranInicialString));

                         //   nombres = lista_mapa_nombres_vacia;
                             back.setVisibility(View.VISIBLE);
                             quit.setVisibility(View.GONE);

                            startActivity(intent);


                }

            }
        });

        //BOTON back
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {



                    /*
                   //para volver tras finished, 13/04/2021
                    Intent intent = new Intent(QuizActivity.this, AntMonitorMainActivity.class);


                    actualizacion_datos_activity = true;
                    PrivacyLeaksReportFragment.quiz_inicio_realizado = true;
                    intent.putExtra(ACTUALIZACION_DATOS_ACTIVITY, actualizacion_datos_activity);
                    startActivity(intent);

                     */

                    //test para de un cuestionario a otro.
                //para volver tras finished, 13/04/2021
                Intent intent = new Intent(QuizActivity.this, AntMonitorMainActivity.class);


                actualizacion_datos_activity = true;
                PrivacyLeaksReportFragment.quiz_inicio_realizado = true;
                intent.putExtra(ACTUALIZACION_DATOS_ACTIVITY, actualizacion_datos_activity);
                startActivity(intent);


                }


        });





    }

    private void updateQuestion(){
        mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
        mButtonChoice1.setText(mQuestionLibrary.getChoice1(mQuestionNumber));
        mButtonChoice2.setText(mQuestionLibrary.getChoice2(mQuestionNumber));
        mButtonChoice3.setText(mQuestionLibrary.getChoice3(mQuestionNumber));

        mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
        mQuestionNumber++;

        //test para quitar los botones cuando no hagan falta
        if(mQuestionView.getText() =="Finished, just answer a few questions more in order to know what you think and expect about the app. EVERYTHING IS ANONYMOUS.") {
            mButtonChoice1.setVisibility(View.GONE);
            mButtonChoice2.setVisibility(View.GONE);
            mButtonChoice3.setVisibility(View.GONE);
            quit.setVisibility(View.VISIBLE);

        }
        if(mQuestionView.getText() !="Finished, just answer a few questions more in order to know what you think and expect about the app. EVERYTHING IS ANONYMOUS.") {
           quit.setVisibility(View.GONE);


        }
    }


    private void updateScore(int point) {
        mScoreView.setText("" + mScore);
    }



}
