package com.lianwei.adapter;

/**
 * 创建实现了 AdvancedMediaPlayer 接口的实体类。
 * @author chenpeng
 */
public class Mp4Player implements AdvancedMediaPlayer{
    @Override
    public void playVlc(String fileName) {

    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: "+ fileName);
    }
}
