package trainedge.qaadmin.Models;

import android.support.annotation.NonNull;

/**
 * Created by Nikita on 10-Jun-17.
 */

public class Category implements Comparable {
    String name;

    public Category(String name) {

        this.name = name;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }
}
