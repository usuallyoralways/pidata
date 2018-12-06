#!/usr/bin/python
# -*- coding: UTF-8 -*-
import json
import random
class Data_handle:
    def __init__(self):
        self.values=list()
        self.jsons=dict()
        self.accs = [0.001,0.002,0.005,0.01,0.02,0.05,0.1,0.2,0.5,1,2,5,10,20,50,100,]

##得到查找精度
    def get_acc(self, a, b, block):
        c = (b-a)/block
        if c==0:
            return 0
        if c>0 and c<=0.2:
            return 0.001
            return 0.001
        if c > 0.2 and c <= 5:
            return 0.1
        if c > 5 and c <= 20:
            return 0.1
        if c>20  and c<=50:
            return 0.1
        if c>50 and c<=100:
            return 0.1
        return 1

    def read_data(self):
        with open("mp.data.1.AllData",encoding='utf-8') as ftr:
            line = ftr.readline()
            line = ftr.readline()
            i=1
            while line:

                print('load train:'+str(i))
                line=line.split("\t")

                self.values.append(float(line[3]));
                line = ftr.readline()
                i += 1
        print(len(self.values))
        i=0
        min=100000.0
        max=0
        f = open("data3.csv",mode='w+')
        self.values.sort(key=None, reverse=False)
        block =30


        random.randint(0,1000000)
        while i< int(len(self.values)):
            j=0
            flag=1
            a=self.values[i]
            while (j<block or flag==1) and i<len(self.values):
                if j>=block-1:
                    flag=0
                    if i!=len(self.values)-1:
                        if self.values[i]!=self.values[i+1]:
                            flag=0
                            b=self.values[i]
                            bid= random.randint(0,10000000)
                            (self.jsons[bid])=list()
                            print((self.jsons[bid]))
                            while int(len(self.jsons[bid]))!=0:
                                bid = random.randint(0, 10000000)
                            self.jsons.setdefault(bid,[]).append(a)
                            self.jsons.setdefault(bid,[]).append(b)
                            self.jsons.setdefault(bid,[]).append((self.get_acc(a,b,block)))
                i+=1
                j+=1
                print(str(i)+"aaa\t"+str(j))

        i=0

        while i<int(len(self.values)):
            f.write(str(self.values[i]))
            f.write(',')
            if self.values[i]<min:
                min=self.values[i]
            if self.values[i]>max:
                max=self.values[i];
            i+=1
        print(str(min)+"min")
        print(str(max)+"max")




        with open("data4.json","w+") as fi:
            fi.write(json.dumps(self.jsons))
            fi.close()

        print(self.jsons)
        ftr.close()
        f.close()



if __name__ == '__main__':
    A = Data_handle()
    A.read_data()