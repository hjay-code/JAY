package com.hjay.sdp.principles.lsp.after;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 长方形类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rectangle implements Quadrilateral{
    private double length;
    private double width;
}
