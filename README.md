# Инструкция для запуска проекта 
В командную строку в соответствии с данным шаблоном вводятся параметры: 
usage: LogAnalyzer [--filter-field <arg>] [--filter-value <arg>] [--format
       <arg>] [--from <arg>] [--path <arg>] [--to <arg>]

     --filter-field <arg>    field by which logs will be filtered, only works in
                             combination with filter-value, Possible values:
                             [METHOD, STATUS, REMOTE_ADDRESS, BYTES_SENT]

     --filter-value <arg>    field value by which logs will be filtered

     --format <arg>          output file format, only adoc and markdown are
                             supported

     --from <arg>            start time of logs that should be analysed
                             (format: dd/MMM/yyyy:HH:mm:ss Z)

     --path <arg>            local path or URL to NGINX log file

     --to <arg>              end time of logs that should be analysed 
                             (format: dd/MMM/yyyy:HH:mm:ss Z")

Обязательными являются только параметры --path и --format, --filter-format и --filter-field работают только вместе,
если from и to date не будут выбраны пользователем, то они автоматически определятся как MIN и MAX date из класса OffsetDateTime

Выходной файл автоматически создается в рабочей директории проекта и называется: <имя входного файла>-log-statistics.<adoc или md в зависимости от параметра --filter>
