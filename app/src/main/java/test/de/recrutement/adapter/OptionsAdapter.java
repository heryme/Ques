package test.de.recrutement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;


import test.de.recrutement.R;
import test.de.recrutement.model.QuestionnaireModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Padaliya on 2/22/2017.
 */
public class OptionsAdapter extends ArrayAdapter<QuestionnaireModel.Questions.Options> {
    private static final String TAG = OptionsAdapter.class.getSimpleName();
    private Context context;
    public static List<QuestionnaireModel.Questions.Options> optionsListList;
    private List<String> tempCheckedOptionList, resultList, totalCorrectAns;

    public OptionsAdapter(Context context,
                          int resource,
                          List<QuestionnaireModel.Questions.Options> optionsListList,
                          String que) {
        super(context, resource, optionsListList);
        this.context = context;
        this.optionsListList = optionsListList;
        tempCheckedOptionList = new ArrayList<>();
        resultList = new ArrayList<>();
        totalCorrectAns = new ArrayList<>();
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.row_options, parent, false);
        }
        resultList.clear();
        final QuestionnaireModel.Questions.Options options = getItem(position);
        final CheckBox chkbox_row_option = (CheckBox) view.findViewById(R.id.chkbox_row_option);
        chkbox_row_option.setChecked(false);
        chkbox_row_option.setText(options.getOpText());
        Log.d(TAG, "" + chkbox_row_option.isChecked());

        chkbox_row_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Selected Option-->" + chkbox_row_option.getText());
                Log.d(TAG, "Selected Option Id -->" + options.getOpOptionId());
                Log.d(TAG, "Is Ans-->" + options.getOpIsAnswer());


                if (chkbox_row_option.isChecked()) {
                    if (!tempCheckedOptionList.contains(options.getOpOptionId()))
                        tempCheckedOptionList.add(options.getOpOptionId());
                    resultList.add(options.getOpIsAnswer());
                } else {
                    if (tempCheckedOptionList.contains(options.getOpOptionId()))
                        tempCheckedOptionList.remove(options.getOpOptionId());
                    resultList.remove(options.getOpIsAnswer());

                }
            }
        });

        return view;
    }

    /**
     * Get Next Question
     *
     * @param optionsListList
     */
    public void upadteQuestion(List<QuestionnaireModel.Questions.Options> optionsListList) {
        this.optionsListList.clear();
        tempCheckedOptionList = new ArrayList<>();
        this.optionsListList.addAll(optionsListList);
        notifyDataSetChanged();
    }

    public List<String> getCheckedOptionList() {
        if (tempCheckedOptionList != null && tempCheckedOptionList.size() > 0) {
            return tempCheckedOptionList;
        } else
            return null;
    }

    /**
     * Calculate Final Result
     *
     * @return
     */
    public Double calculationResult() {
        Double totalCorrect = 0.0, totalIncorrect = 0.0, result;
        List<String> correctUserAnsList = new ArrayList<>();
        List<String> incorrectUserAnsList = new ArrayList<>();
        correctUserAnsList.clear();
        incorrectUserAnsList.clear();

        Log.d(TAG, "resultList Size--->" + resultList.size());

        for (int i = 0; i < resultList.size(); i++) {
            Log.d(TAG, "resultList Data--->" + resultList.get(i));
            if (resultList.get(i).equals("1")) {
                correctUserAnsList.add(resultList.get(i));
            } else {
                incorrectUserAnsList.add(resultList.get(i));
            }
        }
        Log.d(TAG, "Actual correctAnsList Size-->" + correctUserAnsList.size());
        Log.d(TAG, "Actual incorrectAnsList Size-->" + incorrectUserAnsList.size());

        if (resultList != null && resultList.size() > 0) {
            totalCorrect = Double.valueOf(correctUserAnsList.size()) / Double.valueOf(totalCorrectAnsFromAPI().size());
            totalIncorrect = Double.valueOf(Double.valueOf(incorrectUserAnsList.size())/Double.valueOf(totalCorrectAnsFromAPI().size()));
        }

        //For If Value Divided 0
        if (!totalIncorrect.isInfinite()) {
            result = totalCorrect - totalIncorrect;
            Log.d(TAG, "Before Result -->" + String.valueOf(result));
        } else {
            result = totalCorrect - 0.0;
        }

        // Result should be 0 to 1
        //If Result In Negative Then Pass Value Zero
        if (result < 0.0) {
            result = 0.0;
        }

        if (result > 1) {
            result = 1.0;
        }


        Log.d(TAG, "Final Result -->" + String.valueOf(result));
        return result;
    }

    /**
     * Total Correct Ans Come From API
     *
     * @return
     */
    public List<String> totalCorrectAnsFromAPI() {
        List<String> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < optionsListList.size(); i++) {
            if (optionsListList.get(i).getOpIsAnswer().equals("1")) {
                list.add(optionsListList.get(i).getOpIsAnswer());
            }
        }
        Log.d(TAG, "totalCorrectAnsFromAPI Size-->" + list.size());
        return list;
    }
}
