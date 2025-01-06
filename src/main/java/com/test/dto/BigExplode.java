package com.test.dto;

import com.google.common.collect.Lists;
import com.test.service.Draw;
import org.apache.commons.collections.CollectionUtils;

import java.awt.*;
import java.util.List;

public class BigExplode implements Draw {
    private List<Explode> explode = Lists.newCopyOnWriteArrayList();

    public BigExplode(List<Explode> explode) {
        this.explode = explode;
    }

    @Override
    public void drawMe(Graphics g) {
        if (CollectionUtils.isNotEmpty(explode)) {
            for (Explode explode1 : explode) {
                explode1.drawMe(g);
            }
        }
    }

    public List<Explode> getExplode() {
        return explode;
    }

    public void setExplode(List<Explode> explode) {
        this.explode = explode;
    }
}
