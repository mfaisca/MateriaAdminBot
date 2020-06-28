package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Raijin extends _Default{
	public Raijin() {
		super("Raijin", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{3306};
				break;
			case HP:
				ids = new int[]{8711, 8712};
				break;
			case S1:
				ids = new int[]{3257, 3256};
				break;
			case S2:
				ids = new int[]{3260, 3261};
				break;
			case EX:
				ids = new int[]{8710};
				break;
			case AA:
				ids = new int[]{6661};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}