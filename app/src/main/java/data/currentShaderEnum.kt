package data



enum class AllShaders{
    Wave,
    Ball
}
object Shaders{
    var currentShader: AllShaders = AllShaders.Wave
}