package stage.grade;

import java.awt.Font;
import java.awt.Image;

import object.MultiFrameObject;
import object.Pattern;
import object.SingleObject;
import object.Sound;
import object.Stage;

public class ManagingGradeStage extends Stage {
   private final static int span = 20;
   private double bHeal, cDamage, fDamage, fGaugeDamage, penalty;
   int f_startTime;
   
   private Image
   player_img = getImage("player.gif"),
   ground_img = getImage("ground_moon.png").getScaledInstance(800,100,Image.SCALE_FAST),
   b = getImage("realB.gif").getScaledInstance(20, 40, Image.SCALE_FAST),
   c = getImage("realC.gif").getScaledInstance(30, 40, Image.SCALE_FAST),
   f = getImage("realF.gif").getScaledInstance(50, 90, Image.SCALE_FAST),
   explosion_img = getImage("explosion.gif"),
   background_img = getImage("background_moon.png").getScaledInstance(800,600, Image.SCALE_FAST),
   success = getImage("success.gif"),
   fail = getImage("fail.gif"),
   big_explosion = getImage("picture_violence/fury.gif").getScaledInstance(80, 90,Image.SCALE_FAST);

   public double grade;
   public Player player;
   public boolean hit = false, impact = false;
   public int impactx, impacty;
   public boolean ending = false, endingFin = false;
   
   private Sound army;
   boolean army_played=false;

   @Override
   public void init() {
      switch (getDifficulty()) {
      case EASY:
         grade = 2.4;
         f_startTime=99;
         bHeal = -0.2;
         cDamage = 0.2;
         fDamage = 0.5;
         fGaugeDamage = 6;
         penalty = 5;
         break;
      case MODERATE:
         grade = 2.2;
         f_startTime=10;
         bHeal = -0.2;
         cDamage = 0.3;
         fDamage = 0.6;
         fGaugeDamage = 8;
         penalty = 6;
         break;
      case HARD:
         grade = 2.0;
         f_startTime=8;
         bHeal = -0.2;
         cDamage = 0.4;
         fDamage = 0.7;
         fGaugeDamage = 10;
         penalty = 7;
         break;
      }
      player = new Player(getWidth() / 2, getHeight() - 100, 0, 0,
            player_img, ManagingGradeStage.this);
      player.velocity = 4.0;

      addPattern(new GradePattern('b', b, explosion_img, 0, 1000, bHeal, 0,
            this));
      addPattern(new GradePattern('c', c, explosion_img, 5, 1000, cDamage, 0,
            this));
      addPattern(new GradePattern('f', f, explosion_img, f_startTime, 3000, fDamage,
            fGaugeDamage, this));
      // pattern player SingleObject
      addPattern(new Pattern() {
         private boolean born = false;

         @Override
         public void whenCrash() {
         }

         @Override
         public boolean removeWhen(SingleObject bl) {
            return false;
         }

         @Override
         public boolean inRange(SingleObject bl) {
            return false;
         }

         @Override
         public boolean createWhen() {
            return born == false;
         }

         @Override
         public SingleObject create() {
            born = true;
            return player;
         }
      });
      // pattern of explosion_img SingleObject
      addPattern(new Pattern() {
         @Override
         public boolean inRange(SingleObject bl) {
            return false;
         }

         @Override
         public void whenCrash() {
         }

         @Override
         public boolean removeWhen(SingleObject bl) {
            return ((MultiFrameObject) bl).shouldRemove();
         }

         @Override
         public boolean createWhen() {
            return hit;
         }

         @Override
         public SingleObject create() {
            hit = false;
            return new MultiFrameObject(player.x - 30, player.y - 30,
                  explosion_img, 100);
         }
      });

      addPattern(new Pattern() {
         @Override
         public boolean inRange(SingleObject bl) {
            return false;
         }

         @Override
         public void whenCrash() {
         }

         @Override
         public boolean removeWhen(SingleObject bl) {
            return ((MultiFrameObject) bl).shouldRemove();
         }

         @Override
         public boolean createWhen() {
            return impact;
         }

         @Override
         public SingleObject create() {
            impact = false;
            return new MultiFrameObject(impactx - 20, impacty - 70,
                  big_explosion, 100);
         }
      });
      // pattern of success/fail
      addPattern(new Pattern() {
         private boolean evaluated = false;

         @Override
         public void whenCrash() {
         }

         @Override
         public boolean removeWhen(SingleObject bl) {
            return false;
         }

         @Override
         public boolean inRange(SingleObject bl) {
            return false;
         }

         @Override
         public boolean createWhen() {
            return second() >= 16 && second() <= 23;
         }

         @Override
         public SingleObject create() {
            Image image;
            if (ManagingGradeStage.this.grade >= 3.3) {
               image = success;
               if (!evaluated) {
                  evaluated = true;
                  gauge += 15;
               }
            } else {
               image = fail;
               if (!evaluated) {
                  evaluated = true;
                  gauge -= (3.3 - grade) * penalty * 5;
                  army=getSound("audio/army2.wav");
                  army.loop();
                  army_played=true;
               }
            }
            return new SingleObject(getWidth() / 3, getHeight() / 3, 0, 0,
                  image);
         }
      });
   }

   @Override
   public void play() {
      updateAllPatterns();
   }

   @Override
   public void draw() {
      drawImage(background_img, 0, 0);
      drawAllPatterns();
      drawImage(ground_img, 0, getHeight() - 90);

      setFont(new Font("Default", Font.BOLD, 20));
      switch(getDifficulty()) {
		case EASY:
			drawString("쉬움",100,getHeight()/2+240);
			break;
		case MODERATE:
			drawString("보통",100,getHeight()/2+240);
			break;
		case HARD:
			drawString("어려움",100,getHeight()/2+240);
			break;
		}
      drawString("TIME : " + String.format("%.0f", totalSecond()),
            getWidth() / 2 - 200, getHeight() / 2 + 240);
      drawString("HP : " + String.format("%.0f", gauge),
            getWidth() / 2 - 200, getHeight() / 2 + 260);
      drawString("GRADE : " + String.format("%.1f", grade)+" (목표 : 3.3)",
            getWidth() / 2 - 200, getHeight() / 2 + 280);

   }

   @Override
   public boolean continuing() {
      if (gauge <= 0) {
         stageFailed = true;
         ending = true;
         if (army_played) army.stop();
         return false;
      }
      if(gauge>0 && second()>span){
    	 if (army_played) army.stop();
         return false;
      }
      return second() <= span;
   }
}
