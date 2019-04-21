from imageai.Detection import ObjectDetection
import os
import urllib.request

execution_path = os.getcwd()

detector = ObjectDetection()
detector.setModelTypeAsRetinaNet()
detector.setModelPath("/home/abhi/MajorProject/resnet50_coco_best_v2.0.1.h5")
detector.loadModel()
custom_objects = detector.CustomObjects(person=True, car=False)
detections = detector.detectCustomObjectsFromImage(input_image="/home/abhi/Documents/MajorProject2k19/uploads/image1.jpeg", output_image_path="/home/abhi/Documents/MajorProject2k19/uploads/image_new1.jpg", custom_objects=custom_objects, minimum_percentage_probability=65)


for eachObject in detections:
   print(eachObject["name"] + " : " + eachObject["percentage_probability"] )
   print("--------------------------------")
flag=0
for eachObject in detections:
    if eachObject["name"]=='person' and float(eachObject["percentage_probability"])>20.0 :
        print("true")
        flag=1
        contents = urllib.request.urlopen("http://blynk-cloud.com/5e14c9d41f2645cd8b72ed26758e7d2e/update/D2?value=0").read()
        break
if (flag==0):
    print("false")
    contents = urllib.request.urlopen("http://blynk-cloud.com/5e14c9d41f2645cd8b72ed26758e7d2e/update/D2?value=1").read()