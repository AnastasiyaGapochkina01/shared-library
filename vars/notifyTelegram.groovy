def call(String prefix, String token, String chatId) {
  def jobName = env.JOB_NAME ?: "unknown job"
  def buildStatus = currentBuild.currentResult ?: "UNKNOWN"
  def buildUrl = env.BUILD_URL ?: ""

  def template = libraryResource 'telegramMessageTemplate.md'
  def message = template
      .replace('${jobName}', jobName)
      .replace('${buildStatus}', buildStatus)
      .replace('${prefix}', prefix)
      .replace('${buildUrl}', buildUrl)


  sh """
    curl -s -X POST https://api.telegram.org/bot${token}/sendMessage \
      -d chat_id=${chatId} \
      -d parse_mode=Markdown \
      -d text="${message.replace("\"", "\\\"")}"
  """
}
