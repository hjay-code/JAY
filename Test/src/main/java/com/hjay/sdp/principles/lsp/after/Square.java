package com.hjay.sdp.principles.lsp.after;

import com.hjay.sdp.principles.lsp.before.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 正方形
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Square implements Quadrilateral {

    private double side;


    @Override
    public double getLength() {
        return side;
    }

    @Override
    public double getWidth() {
        return side;
    }
}
