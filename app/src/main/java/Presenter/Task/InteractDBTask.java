package Presenter.Task;

import Model.DB.PlayerTypeStat;
import Model.DB.PlayerTypeStatsDatabase;
import Model.GamePlay;
import android.os.AsyncTask;
import android.content.Context;

import java.util.List;

/**
 * Async task to interact with database
 */
public class InteractDBTask extends AsyncTask<Void, Void, Void> {

    /**
     * Loads and sets the Neural Network of PlayerLogic
     *
     * @param  context   Context
     */
    public List<PlayerTypeStat> LoadPlayerTypeDB(Context context){

        List<PlayerTypeStat> db_data = PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().getAllStats();

        return db_data;

    }

    /**
     * Loads and sets the Neural Network of PlayerLogic
     *
     * @param  context   Context
     * @param db_data
     */
    public void SavePlayerTypeDB(Context context, PlayerTypeStat[] db_data){

        PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().insertStats(db_data);

    }

    /**
     * Loads and sets the Neural Network of PlayerLogic
     *
     * @param  context   Context
     */
    public void IncreaseWinner(Context context, GamePlay game_play){

        List<PlayerTypeStat> db_data = PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().getAllStats();

        int number_of_victories = 0;

        String winner_type = game_play.WinnerType();

        if(db_data != null){

            for(int i = 0; i < db_data.size(); i++){

                if(db_data.get(i).component2().equals(winner_type) && winner_type.equals("AI")){

                    number_of_victories = db_data.get(i).component3();

                    PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().deleteStat(db_data.get(i));

                    number_of_victories++;
                    db_data.get(i).setNumberOfVictories(number_of_victories);

                    PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().insertStats(db_data.get(i));

                }

                else if(db_data.get(i).component2().equals(winner_type) && winner_type.equals("human")){

                    number_of_victories = db_data.get(i).component3();

                    PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().deleteStat(db_data.get(i));

                    number_of_victories++;
                    db_data.get(i).setNumberOfVictories(number_of_victories);

                    PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().insertStats(db_data.get(i));

                }

            }

        }

        else {

            PlayerTypeStat AI_stat;
            PlayerTypeStat human_stat;

            for(int i = 0; i < db_data.size(); i++){

                if(winner_type.equals("AI")){

                    number_of_victories = db_data.get(i).component3();

                    AI_stat = new PlayerTypeStat(null,"AI",1);
                    human_stat = new PlayerTypeStat(null,"human",0);

                    PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().insertStats(AI_stat);
                    PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().insertStats(human_stat);

                }

                else if(winner_type.equals("human")){

                    AI_stat = new PlayerTypeStat(null,"AI",0);
                    human_stat = new PlayerTypeStat(null,"human",1);

                    PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().insertStats(AI_stat);
                    PlayerTypeStatsDatabase.Companion.getInstance(context).PlayerTypeStatDao().insertStats(human_stat);

                }

            }

        }

    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;

    }
}
