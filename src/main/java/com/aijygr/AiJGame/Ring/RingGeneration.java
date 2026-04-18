package com.aijygr.AiJGame.Ring;


import com.aijygr.AiJGame.Game;
import com.aijygr.LIB;


public class RingGeneration {
    public enum GenerationMode {
        UNIFORM,            // 完全随机
        EDGE_WEIGHTED,      // 边缘加权
        MID_WEIGHTED,       // 中心加权
        TANGENT,            // 切角
        RANDOM              // 随机模式
    }
    public enum WeightedMode{
        AVG2,
        MUL2
    }

    public static LIB.VecIntXZ Generate(int curr_x, int curr_z, double curr_size, double next_size)
    {
        return Generate(curr_x,curr_z,curr_size,next_size,GenerationMode.RANDOM);
    }
    public static LIB.VecIntXZ Generate(int curr_x, int curr_z, double curr_size, double next_size, GenerationMode MODE)
    {
        int next_x = curr_x, next_z = curr_z;
        float range_xz = (int)((curr_size - next_size)/2);
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
        double ix=0.0d,iz=0.0d;
        switch (MODE) {
            case UNIFORM:
                next_x = (int)Math.round((Math.random()*2-1)*range_xz)+curr_x;
                next_z = (int)Math.round((Math.random()*2-1)*range_xz)+curr_z;
                break;
            case MID_WEIGHTED:
            case EDGE_WEIGHTED:
                switch(Game.r_weighted_mode){
                    case AVG2:
                        ix = ((Math.random()*2-1)+(Math.random()*2-1))/2;
                        iz = ((Math.random()*2-1)+(Math.random()*2-1))/2;
                        break;
                    case MUL2:
                        ix = ((Math.random()*2-1)*(Math.random()*2-1));
                        iz = ((Math.random()*2-1)*(Math.random()*2-1));
                        break;
                }
                switch(MODE){
                    case MID_WEIGHTED:
                        break;
                    case EDGE_WEIGHTED:
                        if(ix<=0)
                            ix = (-1.0d -ix);
                        else ix = (1.0d -ix);
                        if(iz<=0)
                            iz = (-1.0d-iz);
                        else iz = (1.0d-iz);
                        break;
                }
                next_x = (int)Math.round((ix*range_xz)+curr_x);
                next_z = (int)Math.round((iz*range_xz)+curr_z);
                break;
            case TANGENT:
                ix = ((Math.random()*2-1)<=0? -1.0d : +1.0d);
                iz = (Math.random()*2-1);
                if((Math.random()*2-1)<=0){
                    double temp =ix;
                    ix = iz;
                    iz = temp;
                } //swap(ix,iz);
                next_x = (int)Math.round((ix*range_xz)+curr_x);
                next_z = (int)Math.round((iz*range_xz)+curr_z);
                break;
        }
        System.out.println(MODE.name()+":("+curr_size+","+next_size+")");
        return new LIB.VecIntXZ(next_x,next_z).sout();
    }
}
