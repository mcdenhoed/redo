import pygame
import sys, os

class Actor(pygame.sprite.Sprite):
    grav = 2.9
    maxVel = 70
    velDamp = .1
    accDamp = .35
    accDefault = 3
    groundAcc = 8.4
    airAcc = 2.7
    left, right, onGround = False, False, False
    def __init__(self, x = 100.0, y = 100.0):
        print "Test"
        pygame.sprite.Sprite.__init__(self)
        self.pos = [x,y]
        self.vel = [0.0,0.0]
        self.acc = [0.0, Actor.grav]
        self.theta = 0.0
        print "Yo"
        self.dtheta = 0.0
        imgpath = os.path.join("assets", "images", "coolball.png")
        self.image = pygame.image.load(imgpath).convert_alpha()
        self.rect = self.image.get_rect()
        
    def update(self, offset=[0.0, 0.0]):
        self.pos = [a[0]+a[1]+Actor.velDamp*a[2] for a in zip(self.pos, offset, self.vel)]
        #On above line: self.pos = [a +b + Actor.velDamp*c for a, b, c in zip(stuff)]
        if self.vel[0] > self.maxVel:
            self.acc[0] = 0

        self.vel = [a[0]+Actor.accDamp*a[1] for a in zip(self.vel, self.acc)]
        
        if not (self.left or self.right):
            if (self.onGround):
                self.acc[0] = -.2*self.vel[0]
            else:
                self.acc[0] = -.12*self.vel[0]

        self.rect.left, self.rect.top = int(self.pos[0]), int(self.pos[1])
://mail.google.com/mail/u/0/#inbox
        if self.left:
            self.leftPress()
            self.dtheta = -5
        elif self.right:
            self.rightPress()
            self.dtheta = 5
        else:
            self.dtheta *= .95
        self.theta += self.dtheta
        self.theta = self.theta%360

    def leftPress(self):
        if self.onGround: self.acc[0] = -Actor.groundAcc
        else: self.acc[0] = -Actor.airAcc

    def rightPress(self):
        if self.onGround: self.acc[0] = Actor.groundAcc
        else: self.acc[0] = Actor.airAcc      
