package com.aijygr.Events.Game.Ring;


import com.aijygr.Events.Game.Game;


public class RingGeneration {
    public enum GenerationMode {
        UNIFORM,    // 完全随机
        EDGE_WEIGHTED,   // 加权
        MID_WEIGHTED,
        TANGENT,    // 切角
        RANDOM
    }
    public enum WeightedMode{
        AVG2,
        MUL2
    }
    public RingGeneration(int curr_x, int curr_z, double curr_size,double next_size)
    {
        RingGeneration(curr_x,curr_z,curr_size,next_size,GenerationMode.RANDOM);
    }
    public Game.VecIntXZ RingGeneration(int curr_x, int curr_z, double curr_size,double next_size, GenerationMode MODE)
    {
        int next_x=0, next_z=0;
        int range_xz = (int)Math.floor(curr_size - next_size);
        if(MODE==GenerationMode.RANDOM)
        {
            int randomNum = (int)(Math.random() * 4) + 1;
            switch(randomNum)
            {
                case 2:
                    MODE = GenerationMode.EDGE_WEIGHTED;
                    break;
                case 3:
                    MODE = GenerationMode.MID_WEIGHTED;
                    break;
                case 4:
                    MODE = GenerationMode.TANGENT;
                    break;
                case 1:
                default:
                    MODE = GenerationMode.UNIFORM;
            }
        }

        switch (MODE) {
            case UNIFORM:
                next_x = (int)((Math.random()*2-1)*range_xz)+curr_x;
                next_z = (int)((Math.random()*2-1)*range_xz)+curr_z;
                break;
            case EDGE_WEIGHTED:
                break;
            case TANGENT:
                break;
        }
        return new Game.VecIntXZ(0,0);
    }
}
