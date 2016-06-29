package ru.znay.znay.tt.tool;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 29.06.2016.
 */
public class R implements Disposable {

    private List<Disposable> disposables = new ArrayList<Disposable>();

    public static R i = new R();

    private R() {

    }

    public <T> T register(Disposable disposable) {
        disposables.add(disposable);
        return (T) disposable;
    }

    public void dispose() {
        for (Disposable d : disposables) {
            d.dispose();
        }
    }
}
