

var i = 1;
var string="image";

while (i)
{
   var target = UIATarget.localTarget();
   target.delay(0.05);
   target.captureScreenWithName(string);
   //runCommand("sips","-s","format","jpeg","-s","formatOptions","60","/AppTestLog/Run 1/image.png","-o","/AppTestLog/image.jpg");
   
   i = i + 1;    
   if (i == 100)
   {
       i =0;
   }
   
}
