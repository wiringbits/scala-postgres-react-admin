package net.wiringbits.spra.admin.modules

import net.wiringbits.spra.admin.tasks.DataExplorerConfigValidatorTask
import play.api.inject
import play.api.inject.SimpleModule

class AdminModule extends SimpleModule(inject.bind[DataExplorerConfigValidatorTask].toSelf.eagerly())
