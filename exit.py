from levelformat import Exit as e
import os
import pygame

class Exit(pygame.sprite.Sprite):
    image = None
    
    def __init__(self, exitFormat):
        pygame.sprite.Sprite.__init__(self)
        if not Exit.image:
            imgpath = os.path.join("assets", "images", "exit.png")
            Exit.image = pygame.image.load(imgpath).convert_alpha()
        self.image = Exit.image
        self.rect = self.image.get_rect()
        self.initialpos = self.pos = self.rect.center = exitFormat.rect.center
    
    def update(self, offset):
        self.pos = [a + b for a,b in zip(self.pos, offset)]
        self.rect.center = self.pos
    
    def reset(self):
        self.pos = self.initialpos
        self.rect.center = self.initialpos
