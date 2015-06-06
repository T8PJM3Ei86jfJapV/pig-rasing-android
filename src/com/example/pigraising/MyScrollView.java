package com.example.pigraising;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
/**
 * 自定义ScrollView
 * @author Linhai
 *
 */
public class MyScrollView extends ScrollView{
    private Handler handler;  
    private View view;  
	public MyScrollView(Context context) {  
        super(context);          
    }  
    public MyScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  

/*
 * 4.2以上版本会导致scrollview无法滑动
 */
//    public int computeVerticalScrollRange(){  
//        return super.computeHorizontalScrollRange();  
//    }  
//    public int computeVerticalScrollOffset(){  
//        return super.computeVerticalScrollOffset();  
//    }  

    private void init(){  
        
        this.setOnTouchListener(onTouchListener);  
        handler=new Handler(){  
            @Override  
            public void handleMessage(Message msg) {  
                // process incoming messages here  
                super.handleMessage(msg);  
                switch(msg.what){  
                case 1:  //滑动到scrollview底部
                    if(view.getMeasuredHeight() <= getScrollY() + getHeight() + 40) {  
                        if(onScrollListener!=null){  
                            onScrollListener.onBottom();                      
                        }  
                          
                    }else if(getScrollY()==0){  
                        if(onScrollListener!=null){  
                            onScrollListener.onTop();  
                        }  
                    } 
                    
                    break;
                case 2://手指使scrollview滑动，手指未离开屏幕
//                	if(onScrollListener!=null){  
//                        onScrollListener.onScroll(getScrollY());  
//                    }
                	break;
                case 3://手指离开scrollview，可能scrollview由于惯性滑动并未停止
                	if(onScrollListener!=null){  
                        onScrollListener.onScrollStop();
                    }
                	break;
                default:  
                    break;  
                }  
            }  
        };  
          
    }  
    @Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if(onScrollListener!=null){  
            onScrollListener.onScroll(t);  //t是scrollview滑动的当前可视范围内的顶部与scrollview的y=0里距离
        }
	} 
	public int move = 0;
    public int y1 = 0;
    public int y2 = 0;
    
    OnTouchListener onTouchListener=new OnTouchListener(){      	  
        
        public boolean onTouch(View v, MotionEvent event) {  
        	
            switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
            	
            	break;
            case MotionEvent.ACTION_MOVE:
            	handler.sendMessageDelayed(handler.obtainMessage(2), 100);
                break;  
            case MotionEvent.ACTION_UP:  
                if(view!=null&&onScrollListener!=null){  
                    handler.sendMessageDelayed(handler.obtainMessage(1), 800);
                    handler.sendMessageDelayed(handler.obtainMessage(3), 500);
                }  
                
                break;  

            default:  
                break;  
            }  
            return false;  
        }  
          
    };  
    
    /** 
     * 获得参考的View，主要是为了获得它的MeasuredHeight，然后和滚动条的ScrollY+getHeight作比较。 
     */  
    public void getView(){  
        this.view=getChildAt(0);  
        if(view!=null){  
            init();  
        }  
    }  
      
    /** 
     * 定义接口 
     * 
     */  
    public interface OnScrollListener{  
        void onBottom();  
        void onTop();  
        void onScroll(int height);  
        void onScrollStop();
    }  
    private OnScrollListener onScrollListener;  
    public void setOnScrollListener(OnScrollListener onScrollListener){  
        this.onScrollListener=onScrollListener;  
    }  
    
}
