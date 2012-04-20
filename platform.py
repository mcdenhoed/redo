from levelformat import Platform as p
import pygame
import os
class Platform(pygame.sprite.Sprite):
    def __init__(self, platformFormat):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.Surface((platformFormat.rect.width, platformFormat.rect.height)).convert()
        self.rect = self.image.get_rect()
        self.pos = self.rect.center = platformFormat.rect.center
        self.visible = platformFormat.visibleDefault
        self.group = platformFormat.setBy
        
    def update(self, offset):
       self.pos = [a + b for a,b in zip(self.pos, offset)]
       self.rect.center = self.pos
        
