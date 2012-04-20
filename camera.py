import pygame

class Camera(pygame.sprite.Sprite):
    damper = .0015
    def __init__(self, width, height):
        self.pos = [0.0, 0.0]
        self.vel = [0.0, 0.0]
        self.rect = pygame.Rect(0, 0, width, height)
        self.rect.inflate_ip(-width/2, -height/2)
        self.rect.move_ip(-width/4, 0)
        self.minsize = self.rect.size
        #self.rect.move_ip(0, .6*height)

    def update(self, player):
        if not self.rect.collidepoint(player):
            self.vel = [a-b for a,b in zip(self.rect.center, player)]
            self.pos = [a+Camera.damper*b for a,b in zip(self.pos,self.vel)]
            self.growArea()
        else:
            self.pos = [.9*a for a in self.pos]
            print self.rect.size>self.minsize
            if self.rect.size > self.minsize:
                self.shrinkArea()
        return self.pos
    def growArea(self):
        print self.rect.size
        self.rect.inflate_ip(2*self.rect.width, 2*self.rect.height)

    def shrinkArea(self):
        self.rect.inflate_ip(-.5*self.rect.width, -.5*self.rect.height)
