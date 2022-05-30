package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderContent {
    private String name;
    private String count;
    private String image;
    private Type type;
    private Size size;
    private List<Extra> extras;
    private List<Choice> chocies;

    public String getType() {
        return type == null ? "" : type.getName();
    }

    public String getSize() {
        if (size != null)
            return size.getName();
        return "";
    }

    public String getExtras() {
        StringBuilder stringBuilder = new StringBuilder();
        if (extras != null) {
            int size = extras.size();
            for (int i = 0; i < size; i++) {
                stringBuilder.append(extras.get(i).getName());
                if (i != size - 1)
                    stringBuilder.append(" - ");
            }
        }
        return stringBuilder.toString();
    }

    public String getChoices() {
        StringBuilder stringBuilder = new StringBuilder();
        if (chocies != null) {
            int size = chocies.size();
            for (int i = 0; i < size; i++) {
                stringBuilder.append(chocies.get(i).getName());
                if (i != size - 1)
                    stringBuilder.append(" - ");
            }
        }
        return stringBuilder.toString();
    }
}