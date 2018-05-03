package ch.uzh.supersede.feedbacklibrary.components.views;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.RatingBar;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;

public class RatingMechanismView extends AbstractMechanismView {
    private RatingMechanism ratingMechanism = null;
    private RatingBar ratingBar;

    public RatingMechanismView(LayoutInflater layoutInflater, AbstractMechanism mechanism) {
        super(layoutInflater);
        this.viewOrder = mechanism.getOrder();
        this.ratingMechanism = (RatingMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.mechanism_rating, null));
        initView();
    }

    private void initView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_title)).setText(ratingMechanism.getTitle());
        this.ratingBar = ((RatingBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_rating));
        this.ratingBar.setNumStars(ratingMechanism.getMaxRating());
        this.ratingBar.setStepSize(1.0f);
    }

    @Override
    public void updateModel() {
        ratingMechanism.setInputRating(((RatingBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_rating)).getRating());
    }

    public float getRating(){
        return ratingBar.getRating();
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof AbstractMechanismView){
            int comparedViewOrder = ((AbstractMechanismView) o).getViewOrder();
            return comparedViewOrder > getViewOrder() ? -1 : comparedViewOrder == getViewOrder() ? 0 : 1;
        }
        return 0;
    }
}
