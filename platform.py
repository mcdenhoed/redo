from leveleditor import Platform as p
import pygame
import os
class Platform(pygame.sprite.Sprite):
    def __init__(self, platformFormat):
        pygame.sprite.__init__(self)
        self.image = pygame.Surface((platformFormat.rect.width, platformFormat.rect.height)).convert()
        self.rect = self.image.get_rect()
        self.visible = platformFormat.visibleDefault
        self.group = platformFormat.setBy
