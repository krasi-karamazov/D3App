package kpk.dev.d3app.models;

/**
 * Created with IntelliJ IDEA.
 * User: kpkdev
 * Date: 3/24/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleChoiceItemObject {
    private String mTitle;
    private boolean mChecked;
    public SingleChoiceItemObject(String title, boolean checked) {
        this.mTitle = title;
        this.mChecked = checked;
    }

    public SingleChoiceItemObject() {}

    public boolean getisChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
}
