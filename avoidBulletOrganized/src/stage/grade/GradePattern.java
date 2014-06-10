package stage.grade;

import java.awt.Image;
import java.util.Random;

import object.Pattern;
import object.SingleObject;

class GradePattern extends Pattern{
   Image explosion,image;
   int startTime;
   double damage,gaugeDamage;
   double frequency=500;
   private ManagingGradeStage stage;
   private char grade;
   private int cnt=0;
   
   public GradePattern(char grade,Image image,Image explosion,
         int startTime,double frequency,
         double damage,double gaugeDamage,
         ManagingGradeStage stage){
      this.image=image;
      this.explosion=explosion;
      this.startTime=startTime;
      this.damage=damage;
      this.gaugeDamage=gaugeDamage;
      this.frequency=frequency;
      this.stage=stage;
      this.grade=grade;
   }
   
   public SingleObject create() {
      Random rand=new Random();
      double num=rand.nextDouble();
      int pos=rand.nextInt(stage.getWidth());
      if(cnt%2==0 && grade=='f'){
         stage.getSound("audio/missile_fly.wav").play();
      }
      if(grade=='f'){
         cnt++;
      }
      return new SingleObject(pos, 0, (Math.PI/4.0)*(num-0.5), frequency/stage.fps/2.0,image);
   }
   public boolean createWhen(){
      if(grade=='c' || grade=='b')
         return stage.count%(50*stage.fps/frequency+1)==0 && stage.second()>=startTime;
      else
         return stage.count%(300*stage.fps/frequency+1)==0 && stage.second()<=16 && stage.second()>=startTime;
            
   }
   public boolean removeWhen(SingleObject bl){
      boolean remove=bl.inRange(stage.player)
            || !bl.inArea(-20,stage.getWidth()+20,-50,stage.getHeight()-100)
            || stage.second()>17;
      if(remove && grade=='f'){
         stage.getSound("audio/f_explode.wav").play();
         stage.impact=true;
         stage.impactx=(int)bl.x;
         stage.impacty=(int)bl.y;
      }
      return remove;
   }
   public void whenCrash(){
      stage.gauge-=gaugeDamage;
      stage.grade-=damage;
      if (stage.grade>4.5) stage.grade=4.5;
      if (stage.grade<0.0) stage.grade=0.0;
      stage.hit=true;
      if(grade=='f'){
         stage.impact=true;
         stage.impactx=(int)stage.player.x;
         stage.impacty=(int)stage.player.y;
      }
   }
   public boolean inRange(SingleObject bl){
      return bl.inRange(stage.player);
   }

}
