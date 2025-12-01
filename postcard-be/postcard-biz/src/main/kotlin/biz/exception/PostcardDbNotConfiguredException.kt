package biz.exception

import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode

class PostcardDbNotConfiguredException (workMode: PsWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)