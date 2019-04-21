import cv2
from darkflow.net.build import TFNet
import numpy as np
import time
import urllib.request
import os

os.chdir("/home/abhi/Major_Project_All_Content/darkflow-master/")


options = {'model':'cfg/yolo.cfg',
'load':'bin/yolo.weights',
'threshold':0.15
}

tfnet = TFNet(options)
colors = [tuple(255 * np.random.rand(3)) for _ in range(10)]

capture = cv2.VideoCapture('http://192.168.1.7:8080/video')
capture.set(cv2.CAP_PROP_FRAME_WIDTH, 1920)
capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 1080)

while True:
    stime = time.time()
    ret, frame = capture.read()
    if ret:
        flag=0
        results = tfnet.return_predict(frame)
        for color, result in zip(colors, results):
            tl = (result['topleft']['x'], result['topleft']['y'])
            br = (result['bottomright']['x'], result['bottomright']['y'])
            label = result['label']
            confidence = result['confidence']
            text = '{}: {:.0f}%'.format(label, confidence * 100)
            frame = cv2.rectangle(frame, tl, br, color, 5)
            frame = cv2.putText(
                frame, text, tl, cv2.FONT_HERSHEY_COMPLEX, 1, (0, 0, 0), 2)
            if label == 'person':
                flag=1
                print('person detected')
                contents = urllib.request.urlopen("http://blynk-cloud.com/5e14c9d41f2645cd8b72ed26758e7d2e/update/D2?value=0").read()
                break
        cv2.imshow('frame', frame)
        print('FPS {:.1f}'.format(1 / (time.time() - stime)))
        if flag==0:
            print("false")
            contents = urllib.request.urlopen("http://blynk-cloud.com/5e14c9d41f2645cd8b72ed26758e7d2e/update/D2?value=1").read()
    else:
        break
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

capture.release()
cv2.destroyAllWindows()