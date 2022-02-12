package com.github.mjaroslav.game.client.font;

import lombok.var;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Font {
    double getHeight();

    void drawLine(double x, double y, @NotNull String line);

    double getLineWidth(@NotNull String line);

    default @NotNull List<String> splitLineToWidth(@NotNull String line, double width) {
        var result = new ArrayList<String>();
        for (var subLine : line.split("\n")) {
            while (subLine.length() > 0) {
                int i = 0;
                var next = String.valueOf(line.charAt(i));
                while (next.length() < line.length() && getLineWidth(next) <= width) {
                    next += line.charAt(i);
                    i++;
                }
                result.add(next);
                subLine = subLine.substring(i);
            }
        }
        return result;
    }

    default @NotNull String trimLineToWidth(@NotNull String line, double width) {
        int i = 0;
        var result = String.valueOf(line.charAt(i));
        while (result.length() < line.length() && getLineWidth(result) <= width) {
            result += line.charAt(i);
            i++;
        }
        return result;
    }

    void dispose();
}
