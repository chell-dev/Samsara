package me.chell.samsara.api.widget;

import me.chell.samsara.api.Loadable;
import me.chell.samsara.impl.widget.Watermark;

import java.util.ArrayList;
import java.util.List;

public class WidgetManager implements Loadable {
    private List<Widget> widgets;

    @Override
    public void load() {
        widgets = new ArrayList<>();
        widgets.add(new Watermark());

        for(Widget w : widgets) {
            w.load();
        }
    }

    @Override
    public void unload() {

        for(Widget w : widgets) {
            w.unload();
        }

        widgets = new ArrayList<>();
    }

    public List<Widget> getWidgets() {
        return widgets;
    }
}
