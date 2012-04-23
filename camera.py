import pygame

class Camera(pygame.sprite.Sprite):
    damper = .003
    def __init__(self, width, height):
        
        self.pos = [0.0, 0.0]
        self.vel = [0.0, 0.0]
        self.rect = pygame.Rect(0, 0, width, height)
        self.rect.inflate_ip(-.725*width, -.5*height)
        self.rect.move_ip(-width/8, 0)
        self.actualcenter = self.rect.center
        self.minsize = self.rect.size
        #self.rect.move_ip(0, .6*height)
        

    def update(self, player):
        if not self.rect.collidepoint(player):
            self.vel = [a-b for a,b in zip(self.actualcenter, player)]
            self.pos = [a+Camera.damper*b for a,b in zip(self.pos,self.vel)]
            self.growArea()
        else:
            self.pos = [.9*a for a in self.pos]
            if self.rect.size > self.minsize:
                self.shrinkArea()
            else:
                self.rect.size = self.minsize
        return self.pos
    def growArea(self):
        self.rect.inflate_ip(2*self.rect.width, 2*self.rect.height)
        self.rect.center = self.actualcenter

    def shrinkArea(self):
        self.rect.inflate_ip(-.5*self.rect.width, -.5*self.rect.height)
        self.rect.center = self.actualcenter
