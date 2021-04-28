package edu.uci.calit2.federated.client.android.activity;

/**
 * @author Alejandro Aguilera Alcalde 2021
 */
public class QuestionLibrary {

    private String mQuestions [] = {
            "How would you feel about sharing your 'Location' with apps?",
            "How would you feel about sharing your 'Email' with apps?",
           /* "How would you feel about a 'Imei' exposure?",
            "How would you feel about a 'Device ID' exposure?",
            "How would you feel about a 'Serial Number' exposure?",
            "How would you feel about a 'MacAddress' exposure?",

            */
            "How would you feel about sharing your 'Unique Device Identifiers' with apps?",
            "How would you feel about a 'Advertiser Identifier' exposure?",
          /*
            "How would you feel if the exposure has a 'Internal App Server' destination?",
            "How would you feel if the exposure has a 'Advertising Server' destination?",
            "How would you feel if the exposure has a 'Mobile Analytics Server' destination?",
            "How would you feel if the exposure has a 'Social Network Sites Server' destination?",
            "How would you feel if the exposure has a 'Development Server' destination?",

           */
            "Sometimes apps collect your data to be used by the app provider or to be shared with other organizations collaborating with the app provider. How would you feel if the app provider itself is the only organization using your personal data?",
            "How would you feel if another organization different from the app provider (for example, an Advertiser) also uses your personal data?",
            "How would you feel if another organization different from the app provider (for example, an Analytics provider) also uses your personal data?",
            "How would you feel if another organization different from the app provider (for example, an Social Network) also uses your personal data?",

            "Finished, just answer a few questions more in order to know what you think and expect about the app. EVERYTHING IS ANONYMOUS."

    };


    private String mChoices [][] = {
            {"I don't like, no matter what for", "It depends, for example, on what app and for what reasons", "I don't care, I am not paranoid about it"},
            {"I don't like, no matter what for", "It depends, for example, on what app and for what reasons", "I don't care, I am not paranoid about it"},
            {"I don't like, no matter what for", "It depends, for example, on what app and for what reasons", "I don't care, I am not paranoid about it"},
            {"I don't like, no matter what for", "It depends, for example, on what app and for what reasons", "I don't care, I am not paranoid about it"},

            {"I don't like them to get my personal data, even if I must stop using the app", "It depends on the personal data and the app", "I'm ok. They provide the app and I give my data in return"},
            {"I don't like them to get my personal data, even if I must stop using the app", "It depends on the personal data, the app and/or the other organization", "I'm ok. They collaborate with the app provider"},
            {"I don't like them to get my personal data, even if I must stop using the app", "It depends on the personal data, the app and/or the other organization", "I'm ok. They collaborate with the app provider"},
            {"I don't like them to get my personal data, even if I must stop using the app", "It depends on the personal data, the app and/or the other organization", "I'm ok. They collaborate with the app provider"},

            {"", "", ""}
    };



    private String mCorrectAnswers[] = {"Roots", "Leaves", "Flower", "Stem", "Stem", "Stem","Stem","Stem","Stem"};




    public String getQuestion(int a) {
        String question = mQuestions[a];
        return question;
    }


    public String getChoice1(int a) {
        String choice0 = mChoices[a][0];
        return choice0;
    }


    public String getChoice2(int a) {
        String choice1 = mChoices[a][1];
        return choice1;
    }

    public String getChoice3(int a) {
        String choice2 = mChoices[a][2];
        return choice2;
    }

    public String getCorrectAnswer(int a) {
        String answer = mCorrectAnswers[a];
        return answer;
    }

}
