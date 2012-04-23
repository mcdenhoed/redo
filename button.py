from levelformat import Button as bf
import pygame
import os
class Button(pygame.sprite.Sprite):
    images = {}
    def __init__(self, buttonFormat):
        pygame.sprite.Sprite.__init__(self)
        if not Button.images:
            inactiveImgPath = os.path.join("assets", "images", "buttonInactive.png")
            activeImgPath = os.path.join("assets", "images", "buttonActive.png")
            Button.images['inactive'] = pygame.image.load(inactiveImgPath).convert_alpha()
            Button.images['active'] = pygame.image.load(activeImgPath).convert_alpha()
        self.image = Button.images['inactive']
        self.rect = self.image.get_rect()
        self.activated = False
        self.group = buttonFormat.sets
        self.initialpos = self.pos = self.rect.midbottom = buttonFormat.rect.midbottom
    def update(self, offset):
         self.pos = [a + b for a,b in zip(self.pos, offset)]
         self.rect.midbottom = self.pos
         
    def activate(self):
        self.activated = True
        self.image = Button.images['active']

    def deactivate(self):
        self.activated = False
        self.image = Button.images['inactive']
    
    def reset(self):
        self.pos = self.initialpos
        self.rect.midbottom = self.pos
