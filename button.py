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
            Button.images['inactive'] = pygame.image.load(inactiveImgPath)
            Button.images['active'] = pygame.image.load(activeImgPath)
        self.image = Button.images['inactive'].convert_alpha()
        self.rect = self.image.get_rect()
        self.activated = False
        self.group = buttonFormat.sets
        self.pos = self.rect.bottomleft = buttonFormat.rect.bottomleft

    def update(self, offset):
         self.pos = [a + b for a,b in zip(self.pos, offset)]
         self.rect.bottomleft = self.pos
         

