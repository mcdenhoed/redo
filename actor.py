import pygame
import sys, os

class Actor(pygame.sprite.Sprite):
    grav = 6#2.9
    maxVel = 70
    velDamp = .1
    accDamp = .35
    accDefault = 3
    groundAcc = 8.4
    airAcc = 5 
    left, right, onGround, onWall = False, False, False, False
    def __init__(self, acc):
        pygame.sprite.Sprite.__init__(self)
        x, y = acc
        self.pos = [x,y]
        self.vel = [0.0,0.0]
        self.acc = [0.0, Actor.grav]
        self.theta = 0.0
        self.dtheta = 0.0
        imgpath = os.path.join("assets", "images", "rect.png")
        self.image = pygame.Surface((30,30)).convert_alpha()#pygame.image.load(imgpath).convert_alpha()
        self.rect = self.image.get_rect()
        self.initialpos = self.rect.center = self.pos

    def setLocation(self, pos):
        x,y = pos
        self.pos = [x,y]
        self.vel = [0,0]
        self.rect.center = self.pos
    def jump(self):
        if self.onGround is True or self.onWall is True:
            self.vel[1] = -100
            self.onGround, self.onWall = False, False
    def offset(self, x, y):
        self.pos = [a[0] + a[1] for a in zip(self.pos, [x,y])]
        self.rect.center = self.pos

    def update(self, offset=[0.0, 0.0]):
        self.pos = [a+b+Actor.velDamp*c for a,b,c in zip(self.pos, offset, self.vel)]
        #On above line: self.pos = [a +b + Actor.velDamp*c for a, b, c in zip(stuff)]
        if abs(self.vel[0]) > Actor.maxVel and self.acc[0]*self.vel[0] > 0:
            self.acc[0] = 0

        self.vel = [a[0]+Actor.accDamp*a[1] for a in zip(self.vel, self.acc)]
        
        if not (self.left or self.right):
            if (self.onGround):
                self.acc[0] = -.2*self.vel[0]
            else:
                self.acc[0] = -.12*self.vel[0]

        self.rect.center = self.pos 
        if self.left:
            self.leftPress()
        elif self.right:
            self.rightPress()

    def leftPress(self):
        if self.onGround: self.acc[0] = -Actor.groundAcc
        else: self.acc[0] = -Actor.airAcc

    def rightPress(self):
        if self.onGround: self.acc[0] = Actor.groundAcc
        else: self.acc[0] = Actor.airAcc

    def reset(self):
        self.pos = self.initialpos
        self.rect.center = self.pos    
