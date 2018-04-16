package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.MechanismConstants.RATING_TYPE;

public class RatingMechanism extends AbstractMechanism {
    private String ratingIcon;
    private int maxRating;
    private float defaultRating;

    private float inputRating;

    public RatingMechanism(MechanismConfigurationItem item) {
        super(RATING_TYPE, item);
    }

    @Override
    public void handleMechanismParameter(String key, String value) {
        super.handleMechanismParameter(key, value);
        if (key.equals("ratingIcon")) {
            setRatingIcon(value);
        }
        else if (key.equals("maxRating") || key.equals("defaultRating")) {
            Double doubleValue = Double.parseDouble(value);
            setDefaultRating(doubleValue.floatValue());
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    public String getRatingIcon() {
        return ratingIcon;
    }

    public void setRatingIcon(String ratingIcon) {
        this.ratingIcon = ratingIcon;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public float getDefaultRating() {
        return defaultRating;
    }

    public void setDefaultRating(float defaultRating) {
        this.defaultRating = defaultRating;
    }

    public float getInputRating() {
        return inputRating;
    }

    public void setInputRating(float inputRating) {
        this.inputRating = inputRating;
    }
}
